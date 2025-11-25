package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

@Data
public class ESignFieldsReq {
    /**自动签署*/
    private boolean autoExecute;
    /**平台自动签署必传，值为2*/
    private String actorIndentityType;
    /**文档id*/
    private String fileId;
    /**文档位置*/
    private EPosBeanReq posBean;
}
