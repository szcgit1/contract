package com.xtm.contract.enums;

public enum PriceTypeEnum {
    PRICE_AGREEMENT(0, "价证协议"),
    LOCK_PRICE(1, "锁价"),
    CONTRACT(2, "合同");
    private Integer code;
    private String desc;

    PriceTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public static String getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (PriceTypeEnum value : PriceTypeEnum.values()) {
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