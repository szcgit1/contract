package com.xtm.contract.enums;

public enum BearCostEnum {

    /**
     * 运费承担方 0：自担 1：自提 2：回结
     */
    SELF_BARN(0, "自担"),
    SELF_PICK_UP(1, "自提"),
    RETURN_COLLECT(2, "回结");
    private Integer code;
    private String name;
    BearCostEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static String getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (BearCostEnum value : BearCostEnum.values()) {
            if (value.getCode().equals(code)) {
                return value.getName();
            }
        }
        return null;
    }
}
