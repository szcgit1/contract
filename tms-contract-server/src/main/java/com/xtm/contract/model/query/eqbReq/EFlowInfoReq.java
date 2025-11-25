package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

@Data
public class EFlowInfoReq {
    /**是否自动归档，默认false*/
    private boolean autoArchive;
    /**是否自动开启，默认false*/
    private boolean autoInitiate;
    /**文件主题*/
    private String businessScene;
}
