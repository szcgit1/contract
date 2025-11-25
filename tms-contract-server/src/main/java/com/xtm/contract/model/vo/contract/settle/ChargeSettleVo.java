package com.xtm.contract.model.vo.contract.settle;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.settle.ChargeSettleVo
 * @author: wwh
 * @create: 2025-04-03 14:33
 * @description: 充电结算单
 **/
@Data
public class ChargeSettleVo implements Serializable {
    private static final long serialVersionUID = -5293529825466066619L;

    /**
     * 充电期间
     */
    private String chargePeriod;
    /**
     * 结算单号
     */
    private String settleNo;
    /**
     * 充电平台
     */
    private String chargePlatform;
    /**
     * 充电信息
     */
    private List<ChargeInfo> chargeInfos;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 供应商税号
     */
    private String supplierTaxNumber;
    /**
     * 供应商开户行
     */
    private String supplierDepositBankName;
    /**
     * 供应商银行卡号
     */
    private String supplierBankNo;
    /**
     * 买家名称
     */
    private String buyerName;
    /**
     * 买家税号
     */
    private String buyerTaxNumber;
    /**
     * 买家开户行
     */
    private String buyerDepositBankName;
    /**
     * 买家银行卡号
     */
    private String buyerBankNo;
}
