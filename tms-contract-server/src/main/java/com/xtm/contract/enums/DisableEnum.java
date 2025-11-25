package com.xtm.contract.enums;

/**
     * 是否禁用 0启用 1禁用
     */
    public enum DisableEnum {
        STOP(1, "停用"),
        ENABLE(0, "启用");
        private Integer code;
        private String desc;
        DisableEnum(Integer code, String desc) {
            this.code = code;
            this.desc = desc;
        }
        public Integer getCode() {
            return code;
        }
        public String getDesc() {
            return desc;
        }
    }