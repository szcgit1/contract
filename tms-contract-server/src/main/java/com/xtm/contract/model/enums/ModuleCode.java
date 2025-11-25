package com.xtm.contract.model.enums;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/8 10:43
 * @desc
 */
public enum ModuleCode {
    DETAIL("10", "明细合同"),
    FRAMEWORK("20", "框架合同"),
    TEMPLATE("30", "合同模板");

    private String code;
    private String message;

    private ModuleCode(String code, String message) {
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
