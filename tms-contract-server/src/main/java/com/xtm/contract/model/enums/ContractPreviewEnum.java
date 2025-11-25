package com.xtm.contract.model.enums;


/**
 * 合同查看权限控制枚举
 */
public enum ContractPreviewEnum {

    //类型  1 预览电子签章 2预览框架合同 3 预览 无船/无车tob 委托方合同 4预览无船to托运方合同 5甘肃无车tob委托方合同 6甘肃无车to托运方合同
    PREVIEW_ELECTRONIC_SIGNATURE_CONTRACT(1,"预览电子签章"),
    PREVIEW_FRAMEWORK_CONTRACT(2,"预览框架合同"),
    PREVIEW_NO_SHIP_NO_CARGO_TOB_WT_CONTRACT(3,"预览 无船/无车tob 委托方合同"),
    PREVIEW_NO_SHIP_NO_CARGO_TOB_TY_CONTRACT(4,"预览无船to托运方合同"),
    PREVIEW_GANSU_TO_CARGO_TOB_WT_CONTRACT(5,"预览甘肃无车tob委托方合同"),
    PREVIEW_GANSU_TO_CARGO_TY_CONTRACT(6,"预览甘肃无车to托运方合同");

    private Integer type;
    private String message;

    ContractPreviewEnum(Integer type, String message) {
        this.type = type;
        this.message = message;
    }

    public Integer getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
