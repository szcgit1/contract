package com.xtm.contract.feign;

import com.alibaba.fastjson.JSONObject;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.model.Result;
import com.xtm.file.feign.FileServerCenterFeign;
import com.xtm.file.model.vo.FileInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
public class TmsFileService {

    @Resource
    private FileServerCenterFeign fileServerCenterFeign;

    /**
     *  根据id查询文件信息
     */
    public FileInfoVo getFileById(String id){
        Result<FileInfoVo> fileResult = fileServerCenterFeign.findFileById(id);
        log.info("查询文件信息，入参：{}，返回：{}", id, JSONObject.toJSONString(fileResult));
        if (fileResult.isSuccess()){
            return fileResult.getData();
        }
        throw new BusinessException("获取文件失败:"+fileResult.getMessage());
    }

    /**
     *  根据ids查询文件信息
     */
    public List<FileInfoVo> listByIds(List<String> ids) {
        Result<List<FileInfoVo>> fileResult = fileServerCenterFeign.findFileByIds(ids);
        log.info("查询文件信息:{}",fileResult);
        if (fileResult.isSuccess()){
            return fileResult.getData();
        }
        throw new BusinessException("获取文件失败:"+fileResult.getMessage());
    }
}
