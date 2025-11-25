package com.xtm.contract.enums;

public enum SaleTypeEnum {
    LOCK_PRICE(0, "锁价"),
    AGREEMENT(1, "协议"),
    TO_PORT_PRICE(2, "到港定价"),
    UNION_SALE(3, "联合销售"),
    LOCK_PRICE_AGREEMENT(4, "锁价协议");
    private Integer code;
    private String desc;

    SaleTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static String getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SaleTypeEnum value : SaleTypeEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getDesc();
            }
        }
        return null;
    }
    public static Integer getByDesc(String desc) {
        for (SalesContractTypeEnum value : SalesContractTypeEnum.values()) {
            if (value.getDesc().equals(desc)) {
                return value.getCode();
            }
        }
        return null;
    }
    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}