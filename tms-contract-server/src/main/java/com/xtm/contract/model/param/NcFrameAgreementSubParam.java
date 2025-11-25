package com.xtm.contract.model.param;

import lombok.Data;

import java.math.BigDecimal;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:49
 *@Description: 保存NC框架合同协议子表入参
 */
@Data
public class NcFrameAgreementSubParam {

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

    /**
     * nc子表主键
     */
    private String subId;

}
