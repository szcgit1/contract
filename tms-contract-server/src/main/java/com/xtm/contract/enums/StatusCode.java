package com.xtm.contract.enums;

/**
 *   状态枚举
 */
public enum StatusCode {

    OK(200),
    CHECK(420),
    ERROR(500);

    private final Integer code;

    StatusCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
