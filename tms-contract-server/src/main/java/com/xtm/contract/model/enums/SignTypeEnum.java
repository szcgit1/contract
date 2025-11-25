package com.xtm.contract.model.enums;

import com.xtm.contract.enums.DicConstant;

/**
 * @package: com.xiaoniu.contract.model.enums.SignTypeEnum
 * @author: wwh
 * @create: 2025-04-07 10:56
 * @description: 签章文件类型
 **/
public enum SignTypeEnum {
    FIND_CAR_SIGN_TYPE(1, DicConstant.FIND_CAR_CHARGE_SUMMARY_TEMPL,"找车服务费结算单"),
    UNLOAD_CAR_SIGN_TYPE(2,DicConstant.UNLOAD_CAR_CHARGE_SUMMARY_TEMPL,"卸车服务费结算单"),
    SERVICE_SIGN_TYPE(3,DicConstant.SERVICE_CHARGE_SUMMARY_TEMP,"技术服务费结算单"),
    CHARGE_SIGN_TYPE(4,"chargeInvoicesTemp.ftl","充电结算单"),
    PURCHASE_AND_SALE_COMPANY_SIGN_TYPE(5,"PurchaseAndSaleContractCompanyTemplate.ftl","购销合同-公司"),
    PURCHASE_AND_SALE_PERSON_SIGN_TYPE(6,"PurchaseAndSaleContractPersonTemplate.ftl","购销合同-个人"),
    OBD_TYPE(7,"invoicesOBDTempl.ftl","财务-OBD费用管理-公司的"),
    CHARGE_PAYMENT_SLIP_TYPE(8,"chargingDailyBill.ftl","充电日账单"),
    ENERGY_RECE_SUM(9,"energyReceSum.ftl","能源待收汇总单"),
    ENERGY_LONG_DISTANCE_CHARGE_DAILY_BILL_TYPE(10,"longDistanceChargingDailyBill.ftl","长途充电日账单"),

    ENERGY_LONG_DISTANCE_PERIOD_SETTLEMENT_BILL_TYPE(11,"longDistancePeriodSettlementBill.ftl","长途充电周期结算单")
    ;

    private Integer type;
    private String fileName;
    private String desc;

    SignTypeEnum(Integer type, String fileName,String desc) {
        this.type = type;
        this.fileName = fileName;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getFileName() {
        return fileName;
    }

    public String getDesc() {
        return desc;
    }

    public static String getFileName(Integer type) {
        for (SignTypeEnum value : SignTypeEnum.values()) {
            if (type.equals(value.getType())) {
                return value.fileName;
            }
        }
        return null;
    }
}
