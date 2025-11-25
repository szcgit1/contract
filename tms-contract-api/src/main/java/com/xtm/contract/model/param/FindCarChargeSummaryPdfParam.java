package com.xtm.contract.model.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 找车费用汇总PDF响应信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FindCarChargeSummaryPdfParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 找车汇总单ID
     */
    private Long summaryId;

    /**
     * 汇总单编号
     */
    private String summarySheet;

    /**
     * 服务项目
     */
    private String serviceProject;

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
    private Long billingCar;

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
     * 汇总单状态 0：已作废 1：已核销
     */
    private Integer summaryStatus;

    /**
     * 核销时间/制单时间
     */
    private Date writeOffTime;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String modifyUser;

    /**
     * 更新时间
     */
    private LocalDateTime modifyTime;

    /**
     * 是否删除 0：否；1：是
     */
    private Integer isDel;

    /**
     * 本地pdf文件id
     */
    private String localPdfId;

    /**
     * 本地pdf文件路径
     */
    private String localPdfUrl;

    /**
     * 电子签章pdf文件id
     */
    private String ecPdfId;

    /**
     * 电子签章pdf文件路径
     */
    private String ecPdfUrl;

    /**
     * 备注
     */
    private String remark;

    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 签章链接
     */
    private String signLink;

    /**
     * 签章类型 1:找车，2：卸车
     */
    private Integer signType;

}