package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

import java.util.List;

@Data
public class ESignersReq {
    /**
     * 是否平台自动签署，默认false
     * false-为对接平台的用户签署
     * true-平台方自动签署
     * */
    private boolean platformSign;
    /**签署方账号信息*/
    private ESignerAccountReq signerAccount;
    /**签署文件信息*/
    private List<ESignFieldsReq> signfields;
}
