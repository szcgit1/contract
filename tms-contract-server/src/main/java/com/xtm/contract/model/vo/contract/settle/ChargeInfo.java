package com.xtm.contract.model.vo.contract.settle;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.settle.ChargeInfo
 * @author: wwh
 * @create: 2025-04-03 15:08
 * @description: 充电信息
 **/
@Data
public class ChargeInfo implements Serializable {
    private static final long serialVersionUID = 6373638277425025683L;
    /**
     * 充电时长
     */
    private String chargeDate;
    /**
     * 充电电量
     */
    private String chargeTotal;
    /**
     * 充电单价
     */
    private String chargePrice;
    /**
     * 充电金额
     */
    private String chargeAmount;
    /**
     * 税额
     */
    private BigDecimal totalTaxAmount;
    /**
     * 不含税金额
     */
    private BigDecimal totalAmountExcludingTax;
    /**
     * 税率
     */
    private String taxRate;
}
