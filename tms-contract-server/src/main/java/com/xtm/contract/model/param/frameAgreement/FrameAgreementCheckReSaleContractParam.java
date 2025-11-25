package com.xtm.contract.model.param.frameAgreement;

import lombok.Data;

import java.io.Serializable;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 校验关联销售合同入参
 */
@Data
public class FrameAgreementCheckReSaleContractParam implements Serializable {

    /**
     * 合同协议id
     */
    private Long agreementId;

    /**
     * 销售合同id
     */
    private Long contractId;

}