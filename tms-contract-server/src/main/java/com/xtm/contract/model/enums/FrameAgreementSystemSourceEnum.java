package com.xtm.contract.model.enums;

import lombok.Getter;

/**
 * 框架合同协议-系统来源枚举
 */
@Getter
public enum FrameAgreementSystemSourceEnum {

    TMS(0, "TMS"),
    NC(1, "NC"),

    ;
    private final Integer code;//修改来源;
    private final String description;//描述;

    FrameAgreementSystemSourceEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getOperationDesc(Integer key) {
        for (FrameAgreementSystemSourceEnum sourceEnum : FrameAgreementSystemSourceEnum.values()) {
            if (key.equals(sourceEnum.getCode())) {
                return sourceEnum.getDescription();
            }
        }
        return String.valueOf(key);
    }

}