package com.xtm.contract.model.param.frameAgreement;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:30
 *@Description: 框架合同协议关联销售合同入参
 */
@Data
public class FrameAgreementRelateSaleContractParam implements Serializable {

    /**
     * 协议id
     */
    private Long agreementId;

    /**
     * 销售合同id
     */
    private Long contractId;

    /**
     * 关联月份
     */
    private String reMonth;

    /**
     * 关联备注
     */
    @Length(message = "备注上限为{max}个字，下限为{min}个字",min = 0,max = 2000)
    private String reRemark;
}