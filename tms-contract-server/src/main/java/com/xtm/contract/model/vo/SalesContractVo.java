package com.xtm.contract.model.vo;

import lombok.Data;

import java.math.BigDecimal;

/***
 *@Author: 王磊
 *@CreateTime: 2025-08-22  14:28
 *@Description: 销售合同表Vo
 *@title: SalesContractVo
 */
@Data
public class SalesContractVo {


    /**
     * 主键标识
     */
    private Long id;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 年度协议id
     */
    private Long agreementId;

    /**
     * 客户
     */
    private String customerName;

    /**
     * 生效日期
     */
    private String effectiveTime;

    /**
     * 货量
     */
    private BigDecimal totalNumber;

}
