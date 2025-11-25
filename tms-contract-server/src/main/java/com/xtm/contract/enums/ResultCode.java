package com.xtm.contract.enums;

/**
 * @Author: Lu
 * @Description: 接口返回枚举值
 * @Date: 2021/2/26 11:28
 * @Version: 1.0
 */
public enum ResultCode {
    SUCCESS(200,  "操作成功"),
    FAIL(500, "系统异常"),
    FORBIDDEN(403, "没有相关权限"),
    UNAUTHORIZED(401, "暂未登陆或者Token过期"),
    VALIDATOR(400, "数据验证异常"),
    SQL(501, "SQL执行异常");

    private Integer code;
    private String message;

    private ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

}
