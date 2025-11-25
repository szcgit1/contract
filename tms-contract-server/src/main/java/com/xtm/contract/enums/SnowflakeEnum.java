package com.xtm.contract.enums;

public enum SnowflakeEnum {

    FILE("1",  "文件"),
    BANK("2",  "银行"),
    CONTRACT("3",  "合同"),
    NOTICE("4",  "通知"),
    SETTLEMENT("6",  "应结明细"),
    SUPERVISE("8", "监管"),
    ORDER("9", "订单"),
    DISPATCH("10", "调度"),
    TRAJECTORY("11", "轨迹"),
    COMMON("12", "通用"),
    CARGO("13", "货源"),
    ANCILLARY("14", "结算委运"),
    COMPANY("15", "公司"),
    USER("16", "用户"),
    TAX("17", "税务"),
    SETTING("18", "设置"),
    invoice("19", "发票"),
    DRIVER("20", "司机"),
    VEHICLE("21", "载具"),
    TENDERS("22", "招标"),
    OTHER("99", "其他");

    private String code;
    private String message;

    private SnowflakeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
