package com.xtm.contract.model.vo.frameAgreement;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 历史记录-响应内容
 */
@Data
public class FrameAgreementSubHistoryVo {

    /**
     * nc子表主键
     */
    private String subId;

    /**
     * 月份
     */
    private String subMonth;

    /**
     * 数量
     */
    private BigDecimal subAmount;

    /**
     * 合并状态 0：未合并 1：已合并
     */
    private Integer subMergeState;

    /**
     * 备注
     */
    private String subRemark;

    /**
     * 是否关联 0: 否 1: 是
     */
    private Integer subRelate;

    /**
     * 销售合同id
     */
    private String subSaleContractId;

}