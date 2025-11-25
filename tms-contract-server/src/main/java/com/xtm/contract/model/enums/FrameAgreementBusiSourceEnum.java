package com.xtm.contract.model.enums;

import lombok.Getter;

/**
 * 框架合同协议-业务来源枚举
 */
@Getter
public enum FrameAgreementBusiSourceEnum {

    FN(0, "丰南"),
    ZT(1, "中铁"),
    ZZ(2, "中重"),
    TMS(99, "本平台"),

    ;
    private final Integer code;//修改来源;
    private final String description;//描述;

    FrameAgreementBusiSourceEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getOperationDesc(Integer key) {
        for (FrameAgreementBusiSourceEnum sourceEnum : FrameAgreementBusiSourceEnum.values()) {
            if (key.equals(sourceEnum.getCode())) {
                return sourceEnum.getDescription();
            }
        }
        return String.valueOf(key);
    }
}