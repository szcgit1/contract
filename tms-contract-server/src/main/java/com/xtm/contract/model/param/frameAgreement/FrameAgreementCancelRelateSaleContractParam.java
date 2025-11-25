package com.xtm.contract.model.param.frameAgreement;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 框架合同协议取消关联销售合同入参
 */
@Data
public class FrameAgreementCancelRelateSaleContractParam implements Serializable {

    /**
     * 协议id
     */
    private Long agreementId;

    /**
     * 销售合同id
     */
    private List<Long> contractIdList;

}