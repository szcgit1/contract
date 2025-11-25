package com.xtm.contract.model.energy;

import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 结算账单基本信息;
 *
 * @author miaoyouhu
 * @date 2024/4/25 13:39
 */
public class BalanceBaseInfoVo {

    @ApiModelProperty(value = "结算日期：yyyy年MM月dd日")
    private Date balanceDate;

    @ApiModelProperty(value = "结算单号")
    private String statementNumber;

    @ApiModelProperty(value = "运营商平台")
    private String operatorPlatform;

    @ApiModelProperty(value = "供方名称")
    private String supplierName;

    @ApiModelProperty(value = "需求方名称(订单承运公司名称)")
    private String demanderName;
    /**
     * 能源量
     */
    @ApiModelProperty(value = "能源量")
    private BigDecimal totalNum;
    /**
     * 能源类型
     */
    @ApiModelProperty(value = "能源类型")
    private String oilType;
    /**
     * 结算（含税）金额
     */
    @ApiModelProperty(value = "结算（含税）金额")
    private BigDecimal totalTaxIncludedAmount;
    /**
     * 税额
     */
    private BigDecimal totalTaxAmount;
    /**
     * 不含税金额
     */
    @ApiModelProperty(value = "不含税金额")
    private BigDecimal totalPriceExcludingTax;
    /**
     * 税率
     */
    @ApiModelProperty(value = "税率")
    private String taxRate;

    @ApiModelProperty(value = "pdf文件地址")
    private String urlPdf ;

    /** 高灯新加字段开始 **/
    /**
     * 对账月份
     */
    @ApiModelProperty(value = "对账月份")
    private String monthlyBill;
    /**
     * 供应商税号
     */
    @ApiModelProperty(value = "供应商税号")
    private String supplierTaxNumber;
    /**
     * 供应商开户行
     */
    @ApiModelProperty(value = "供应商开户行")
    private String supplierDepositBankName;
    /**
     * 供应商银行卡号
     */
    @ApiModelProperty(value = "供应商银行卡号")
    private String supplierBankNo;
    /**
     * 买家税号
     */
    @ApiModelProperty(value = "买家税号")
    private String buyerTaxNumber;
    /**
     * 买家开户行
     */
    @ApiModelProperty(value = "买家开户行")
    private String buyerDepositBankName;
    /**
     * 买家银行卡号
     */
    @ApiModelProperty(value = "买家银行卡号")
    private String buyerBankNo;

    public String getMonthlyBill() {
        return monthlyBill;
    }

    public void setMonthlyBill(String monthlyBill) {
        this.monthlyBill = monthlyBill;
    }

    public String getSupplierTaxNumber() {
        return supplierTaxNumber;
    }

    public void setSupplierTaxNumber(String supplierTaxNumber) {
        this.supplierTaxNumber = supplierTaxNumber;
    }

    public String getSupplierDepositBankName() {
        return supplierDepositBankName;
    }

    public void setSupplierDepositBankName(String supplierDepositBankName) {
        this.supplierDepositBankName = supplierDepositBankName;
    }

    public String getSupplierBankNo() {
        return supplierBankNo;
    }

    public void setSupplierBankNo(String supplierBankNo) {
        this.supplierBankNo = supplierBankNo;
    }

    public String getBuyerTaxNumber() {
        return buyerTaxNumber;
    }

    public void setBuyerTaxNumber(String buyerTaxNumber) {
        this.buyerTaxNumber = buyerTaxNumber;
    }

    public String getBuyerDepositBankName() {
        return buyerDepositBankName;
    }

    public void setBuyerDepositBankName(String buyerDepositBankName) {
        this.buyerDepositBankName = buyerDepositBankName;
    }

    public String getBuyerBankNo() {
        return buyerBankNo;
    }

    public void setBuyerBankNo(String buyerBankNo) {
        this.buyerBankNo = buyerBankNo;
    }

    /** 高灯新加字段结束 **/



    public BigDecimal getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(BigDecimal totalNum) {
        this.totalNum = totalNum;
    }

    public BigDecimal getTotalTaxIncludedAmount() {
        return totalTaxIncludedAmount;
    }

    public void setTotalTaxIncludedAmount(BigDecimal totalTaxIncludedAmount) {
        this.totalTaxIncludedAmount = totalTaxIncludedAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getTotalPriceExcludingTax() {
        return totalPriceExcludingTax;
    }

    public void setTotalPriceExcludingTax(BigDecimal totalPriceExcludingTax) {
        this.totalPriceExcludingTax = totalPriceExcludingTax;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public String getOperatorPlatform() {
        return operatorPlatform;
    }

    public void setOperatorPlatform(String operatorPlatform) {
        this.operatorPlatform = operatorPlatform;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getDemanderName() {
        return demanderName;
    }

    public void setDemanderName(String demanderName) {
        this.demanderName = demanderName;
    }

    public String getUrlPdf() {
        return urlPdf;
    }

    public void setUrlPdf(String urlPdf) {
        this.urlPdf = urlPdf;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
    }
}