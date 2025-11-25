package com.xtm.contract.utils;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.feign.FileFeignAdapter;
import com.xtm.contract.enums.FileBizSourceEnum;
import com.xtm.common.model.SysUser;
import com.xtm.contract.enums.ServerCode;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.ContractResult;
import com.xtm.contract.model.enums.ContractErrorCode;
import com.xtm.contract.model.enums.FunctionCode;
import com.xtm.contract.model.enums.ModuleCode;
import com.xtm.contract.enums.CommonLang;
import com.xtm.contract.model.query.contractOther.ImageWatermarkInWithBusSource;
import com.xtm.file.constant.FileDirectoryEnum;
import com.xtm.file.model.dto.FileToPdfDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/30 9:59
 * @desc
 */

@Slf4j
@Component
public class FileHelper {

    public static final String ERROR_CODE = "500";

    @Resource
    private FileFeignAdapter fileFeignAdapter;
    @Autowired
    private FileFeignAdapter fileFeignAdapter3;
    @Autowired
    private NacosValueConfig nacosValueConfig;

    /**
     * 通过HTML拿到PDF文件
     * @param content
     * @param agentCode
     * @return
     * @throws Exception
     */
    public FileInfoOut htmlToPdf(String content, String agentCode) throws BusinessException{
        try {
            log.info("文件上传：directory:agentCode={}", agentCode);
            FileToPdfDto pdfIn = new FileToPdfDto();
            pdfIn.setContent(content);
            pdfIn.setAgentCode(nacosValueConfig.getAgentCode());
            pdfIn.setBusinessSource(FileDirectoryEnum.CONTRACT_FILE.getValue());
            SysUser sessionInfo = LoginUserContextHolder.getUser();
            pdfIn.setOperatorUserId(sessionInfo != null ? sessionInfo.getId():null);
            FileInfoOut fileInfoOut = fileFeignAdapter3.toPdf(pdfIn);
            if (fileInfoOut == null) {
                log.info("PDF文件生成失败");
                return null;
            } else {
                if (ERROR_CODE.equals(fileInfoOut.getCode())) {
                    log.info("PDF文件生成失败");
                    return null;
                }
                log.info("PDF文件生成成功：" + JSONObject.toJSONString(fileInfoOut));
                return fileInfoOut;
            }
        }catch (Exception e) {
            log.error("文件服务接口调用转换PDF失败",e);
            throw new BusinessException("文件服务接口调用转换PDF失败:"+e.getMessage());
        }
    }

/**
     * 通过地址上传文件
     * @param url
     * @param agentCode
     * @param fileName
     * @return
     * @throws Exception
     */

    public FileInfoOut urlUploadFile(String url,String agentCode,String fileName){
        log.info("要上传的文件：{}，上传文件的名称为：{}，agentCode是：{}",url,fileName,agentCode);

        ImageWatermarkInWithBusSource watermarkIn = new ImageWatermarkInWithBusSource();
        watermarkIn.setUrl(url);
        watermarkIn.setSource(FileDirectoryEnum.CONTRACT_FILE.getValue());
        watermarkIn.setAgentCode(agentCode);
        watermarkIn.setFileName(fileName);
        watermarkIn.setBizTableName("contract");
        watermarkIn.setBizSource(FileBizSourceEnum.EQB_PDF);
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        watermarkIn.setOperatorUserId(sessionInfo != null ? sessionInfo.getId():null);

        try {
            FileInfoOut fileInfoOut = fileFeignAdapter.urlUploadFileWithBusSource(watermarkIn);
            if (fileInfoOut == null) {
                log.info("文件上传失败");
                return null;
            } else {
                if (ERROR_CODE.equals(fileInfoOut.getCode())) {
                    log.info("文件上传失败：" + JSONObject.toJSONString(fileInfoOut));
                    return null;
                }
                log.info("文件上传成功" + JSON.toJSONString(fileInfoOut));
                return fileInfoOut;
            }
        }catch (Exception exception){
            log.error("文件上传失败"+exception.getMessage(),exception);
            return null;
        }
    }


    /**
     * 文件上传
     * @param uploadFile
     * @param agentCode
     * @return
     * @throws Exception
     */
    public FileInfoOut fileUpload(File uploadFile, String agentCode) {
        log.info("本地文件上传，agentCode是：" + agentCode);
        MultipartFile file = fileToMultipartFile(uploadFile);
        FileInfoOut fileInfoOut = null;
        try {
            SysUser sessionInfo = LoginUserContextHolder.getUser();
            if (StrUtil.isBlank(agentCode)){
                agentCode = nacosValueConfig.getAgentCode();
            }
            fileInfoOut = fileFeignAdapter.uploadFile(file, FileDirectoryEnum.CONTRACT_FILE.getValue(), agentCode,"xn_m_dispatch", FileBizSourceEnum.UPDATE_FDD_PDF_ID,sessionInfo != null ? sessionInfo.getId():null);
        } catch (Exception e) {
            log.error("fileUpload: Exception", e);
            throw new BusinessException(-1,"");
        }
        if (fileInfoOut == null) {
            log.info("文件上传失败");
            return null;
        } else {
            log.info("文件上传完成：{}", JSONObject.toJSONString(fileInfoOut));
            return fileInfoOut;
        }
    }

    public static MultipartFile fileToMultipartFile(File file) {
        FileItem fileItem = createFileItem(file);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        return multipartFile;
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        FileItem item = factory.createItem("textField", "text/plain", true, file.getName());
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(file);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        return item;
    }

    public static File urlToFile(InputStream is) {
        File file = null;
        FileOutputStream fos = null;
        try {
            file = File.createTempFile("tmp", ".pdf");
            fos = new FileOutputStream(file);
            byte[] buffer = new byte[4096];
            int length;
            while ((length = is.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            log.info("urlToFile path={}",file.getPath());
            return file;
        } catch (IOException e) {
            log.error("文件下载失败 e",e);
            throw new BusinessException(CommonLang.SYS_FAIL.getCode(),"文件下载失败");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    log.info("IOException e",e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.info("IOException e",e);
                }
            }
        }
    }
}
