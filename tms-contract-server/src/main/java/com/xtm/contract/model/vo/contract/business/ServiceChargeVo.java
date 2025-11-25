package com.xtm.contract.model.vo.contract.business;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.business.ServcieChargeVo
 * @author: wwh
 * @create: 2025-03-28 15:27
 * @description:
 **/
@Data
public class ServiceChargeVo implements Serializable {
    private static final long serialVersionUID = -7343139414519585315L;
//    /**
//     * 找车汇总单ID
//     */
//    private Long summaryId;

    /**
     * 汇总单编号
     */
    private String summarySheet;


    /**
     * 甲方
     */
    private String firstParty;

    /**
     * 甲方id（承运人id）
     */
    private String firstPartyId;

    /**
     * 甲方统一信用代码
     */
    private String firstCreditCode;
    /**
     * 甲方开户银行
     */
    private String firstDepositBank;
    /**
     * 甲方银行账户
     */
    private String firstBankAccount;

    /**
     * 乙方
     */
    private String secondParty;

    /**
     * 乙方id（服务商id）
     */
    private String secondPartyId;

    /**
     * 乙方统一信用代码
     */
    private String secondCreditCode;
    /**
     * 乙方开户银行
     */
    private String secondDepositBank;
    /**
     * 乙方银行账户
     */
    private String secondBankAccount;

    /**
     * 服务期间
     */
    private String servicePeriod;

    /**
     * 结算车数
     */
    private Integer billingCar;

    /**
     * 含税单价
     */
    private BigDecimal unitPriceTaxIncluded;

    /**
     * 含税金额
     */
    private BigDecimal amountTaxIncluded;

    /**
     * 不含税金额
     */
    private BigDecimal excludingTaxPrice;

    /**
     * 税额
     */
    private BigDecimal taxAmount;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 核销时间/制单时间
     */
    private String writeOffTime;

    /**
     * 备注
     */
    private String remark;
}
