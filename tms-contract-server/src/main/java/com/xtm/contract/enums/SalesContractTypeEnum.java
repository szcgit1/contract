package com.xtm.contract.enums;

public enum SalesContractTypeEnum {
    LOCK_PRICE(0, "锁价基准合同"),
    AGREEMENT(1, "协议基准合同"),
    STEEL_SALE(2, "钢品销售合同"),
    NON_STEEL_SALE(3, "非钢产品销售合同"),
    OTHER(4, "其他合同类型");
    private Integer code;
    private String desc;

    SalesContractTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static String getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (SalesContractTypeEnum value : SalesContractTypeEnum.values()) {
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
}