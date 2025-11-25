package com.xtm.contract.model.vo.frameAgreement;

import lombok.Data;

import java.math.BigDecimal;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:49
 *@Description: 框架合同协议子表
 */
@Data
public class FrameAgreementSubVo {

    /**
     * 主键标识
     */
    private Long id;

    /**
     * 框架合同协议id
     */
    private Long agreementId;

    /**
     * nc子表主键
     */
    private String subId;

    /**
     * 月份
     */
    private String month;

    /**
     * 数量
     */
    private BigDecimal amount;

    /**
     * 合并状态 0：未合并 1：已合并
     */
    private Integer mergeState;

    /**
     * 备注
     */
    private String remark;

}
