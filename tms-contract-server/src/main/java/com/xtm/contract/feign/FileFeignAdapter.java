package com.xtm.contract.feign;

import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.thirdparty.auth.model.resp.FileInfoOut;
import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.model.Result;
import com.xtm.contract.enums.FileBizSourceEnum;
import com.xtm.contract.model.query.contractOther.ImageWatermarkInWithBusSource;
import com.xtm.file.feign.FileServerCenterFeign;
import com.xtm.file.model.dto.FileInfoDto;
import com.xtm.file.model.dto.FileToPdfDto;
import com.xtm.file.model.vo.FileInfoVo;
import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
/**
 * @author ：lll
 * @description：file-feign 适配器
 * @date ：2024/10/09 15:42
 */
@Slf4j
@Component
public class FileFeignAdapter {

    public static final String OK_CODE = "200";

    public static final String ERROR_CODE = "500";

    @Autowired(required = false)
    private FileServerCenterFeign fileServerCenterFeign;

    /**
     * 上传文件
     */
    public FileInfoOut toFileCenter(String agentCode,String source,MultipartFile file,String fileName,String oldUrl,String oldId,String base64,String waterMark,String operatorUserId) throws Exception {
        try {
            FileInfoDto fileInfoDto = new FileInfoDto();
            fileInfoDto.setAgentCode(agentCode);
            fileInfoDto.setBusinessSource(source);
            fileInfoDto.setWaterMark(waterMark);
            fileInfoDto.setOldFileUrl(oldUrl);
            fileInfoDto.setOldFileId(oldId);
            fileInfoDto.setFileName(fileName);
            fileInfoDto.setBase64(base64);
            fileInfoDto.setOperatorUserId(operatorUserId);
            if (file != null) {
                fileInfoDto.setFileByte(file.getBytes());
                fileInfoDto.setFileName(file.getOriginalFilename());
            }
            log.info("dispatchShipment-fileServerCenterFeign-fileInfoDto:{}", fileInfoDto.toString());
            Result<FileInfoVo> result = fileServerCenterFeign.singleUpload(fileInfoDto);
            log.info("dispatchShipment-fileServerCenterFeign-upload:{}", JSONObject.toJSONString(result));
            if (ErrorCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                FileInfoOut fileInfoOut = new FileInfoOut();
                fileInfoOut.setFileUrl(result.getData().getFilePath());
                fileInfoOut.setName(result.getData().getUrl());
                fileInfoOut.setFileID(result.getData().getId());
                fileInfoOut.setFileServerUrl(result.getData().getFileServerUrl());
                fileInfoOut.setCode(OK_CODE);
                return fileInfoOut;
            }
            throw new BusinessException(result.getMessage());
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException("文件上传失败:"+e.getMessage());
        }

    }

    public FileInfoOut uploadFile(MultipartFile file, String source, String agentCode, String bizTableName, FileBizSourceEnum bizSource, String operatorUserId) throws Exception{
        return toFileCenter(agentCode,source,file,null,null,null,null,null,operatorUserId);
    }

    public FileInfoOut urlUploadFileWithBusSource(ImageWatermarkInWithBusSource uploadFileIn) throws Exception{
        return toFileCenter(uploadFileIn.getAgentCode(),uploadFileIn.getSource(),null,uploadFileIn.getFileName(),uploadFileIn.getUrl(),null,uploadFileIn.getBase64(),uploadFileIn.getWaterMark(),uploadFileIn.getOperatorUserId());
    }
    /**
     * @Description: 生成PDF
     */
    public FileInfoOut toPdf(FileToPdfDto fileToPdfDto) throws Exception {
        try {
            log.info("FileFeignAdapter-toPdf-fileToPdfDto-start");
            Result<FileInfoVo> result = fileServerCenterFeign.generatePdf(fileToPdfDto);
            log.info("FileFeignAdapter-toPdf-result:{}", JSONObject.toJSONString(result));
            if (ErrorCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                FileInfoOut fileInfoOut = new FileInfoOut();
                fileInfoOut.setFileUrl(result.getData().getFilePath());
                fileInfoOut.setName(result.getData().getUrl());
                fileInfoOut.setFileID(result.getData().getId());
                fileInfoOut.setFileServerUrl(result.getData().getFileServerUrl());
                fileInfoOut.setCode(OK_CODE);
                return fileInfoOut;
            }
            throw new BusinessException(result.getMessage());
        } catch (Exception e) {
            log.error("生成PDF文件失败", e);
            throw new BusinessException("生成PDF文件失败:"+e.getMessage());
        }
    }

    public Response downLoadToResponseByUrl(String url){
        Response response = null;
        try {
            response = fileServerCenterFeign.getFileByUrl(url);
            if (response.status()!= HttpStatus.OK.value()){
                log.error("下载文件失败:,{},{}",url,response.body().toString());
                throw new BusinessException(500,response.body().toString());
            }
            return response;
        } catch (BusinessException e) {
            log.error("下载文件失败:{}",url,e);
            throw new BusinessException(e.getCode(),e.getMessage());
        }catch (Exception e) {
            log.error("下载文件失败:{}",url,e);
            throw new BusinessException("文件下载失败:"+e.getMessage());
        }
    }

    public Response downLoadToResponseByFileId(String fileId){
        Response response = null;
        try {
            response = fileServerCenterFeign.getFile(fileId);
            if (response.status()!= HttpStatus.OK.value()){
                throw new BusinessException(500,response.body().toString());
            }

            return response;
        } catch (BusinessException e) {
            throw new BusinessException(e.getCode(),e.getMessage());
        }catch (Exception e) {
            throw new BusinessException("文件下载失败:"+e.getMessage());
        }
    }

    /**
     * 持久化文件，将临时文件转正式存储
     * @param fileIdList
     * @return
     */
    public Map<String, FileInfoVo> persistentStorageByIds(@RequestBody List<String> fileIdList) {
        try {
            log.info("持久化文件，将临时文件转正式存储，入参:{}", fileIdList);
            Result<Map<String, FileInfoVo>> result = fileServerCenterFeign.persistentStorageByIds(fileIdList);
            log.info("持久化文件，将临时文件转正式存储，返回:{}", JSONObject.toJSONString(result));
            if (ErrorCodeEnum.SUCCESS.getCode().equals(result.getCode())) {
                Map<String, FileInfoVo> data = result.getData();
                return data;
            }
            throw new BusinessException(result.getMessage());
        } catch (Exception e) {
            log.error("持久化文件，将临时文件转正式存储失败", e);
            throw new BusinessException("持久化文件，将临时文件转正式存储失败:"+e.getMessage());
        }
    }
}
