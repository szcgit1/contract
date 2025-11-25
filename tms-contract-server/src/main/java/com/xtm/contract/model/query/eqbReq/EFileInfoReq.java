package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

@Data
public class EFileInfoReq {
    /**文档id*/
    private String fileId;
    /**文档名称*/
    private String fileName;
    /**文件路径*/
    private String fileUrl;
}
