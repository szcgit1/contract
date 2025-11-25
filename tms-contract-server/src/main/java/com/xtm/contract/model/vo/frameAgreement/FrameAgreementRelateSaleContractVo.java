package com.xtm.contract.model.vo.frameAgreement;

import lombok.Data;

import java.math.BigDecimal;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:28
 *@Description: 框架合同协议id关联的销售合同VO
 */
@Data
public class FrameAgreementRelateSaleContractVo {

    /**
     * 合同id
     */
    private String id;

    /**
     * 合同协议id
     */
    private Long agreementId;

    /**
     * 销售合同编号
     */
    private String contractCode;


    /**
     * 关联月份
     */
    private String reMonth;

    /**
     * 关联货量
     */
    private BigDecimal totalNumber;

    /**
     * 关联合并状态
     */
    private Integer reMergeState;

    /**
     * 关联备注
     */
    private String reRemark;

}
