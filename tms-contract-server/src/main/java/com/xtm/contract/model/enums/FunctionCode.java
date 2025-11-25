package com.xtm.contract.model.enums;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/8 20:20
 * @desc
 */
public enum FunctionCode {
    CON_INSERTUPD("01","新增或修改"),
    CON_DELETE("02", "删除"),
    CON_QUERY("03", "查询"),
    CON_THIRD("04","第三方"),
    CON_COMMON("05","通用");
    private String code;
    private String message;

    FunctionCode(String code, String message) {
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
