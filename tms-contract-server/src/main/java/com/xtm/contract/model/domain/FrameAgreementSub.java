package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.xtm.v1.common.model.TransitionEntity;
import lombok.Data;
import java.math.BigDecimal;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:49
 *@Description: 框架合同协议子表
 */
@TableName(value ="frame_agreement_sub")
@Data
public class FrameAgreementSub extends TransitionEntity<FrameAgreementSub> {

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

    /**
     * 是否关联 0: 否 1: 是
     */
    private Integer relate;

    /**
     * 销售合同id
     */
    private String saleContractId;


}
