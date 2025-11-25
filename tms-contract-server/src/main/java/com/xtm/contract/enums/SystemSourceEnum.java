package com.xtm.contract.enums;

/**
     * 系统来源
     */
    public enum SystemSourceEnum {
        TMS(0, "TMS"),
        NC(1, "NC"),
        XINWANG(2, "新网");
        private Integer code;
        private String desc;
        SystemSourceEnum(Integer code, String desc) {
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