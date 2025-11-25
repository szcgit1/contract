package com.xtm.contract.model.enums;

import lombok.Getter;

/**
 * 业务历史操作-操作类型枚举;
 */
@Getter
public enum FrameAgreementHistoryOperationTypeEnum {

    CREATE("create", "创建"),
    NC_CREATE("ncCreate", "NC创建"),
    UPDATE("update", "更新"),
    UPDATE_STATE("updateState", "修改合同状态"),
    NC_UPDATE_STATE("ncUpdateState", "修改合同状态"),
    RE_LOGIS_CONTRACT("reLogisContract", "关联销售合同"),
    CANCEL_RE_LOGIS_CONTRACT("cancelReLogisContract", "取消关联销售合同"),

    ;
    private final String type;//修改来源;
    private final String description;//描述;

    FrameAgreementHistoryOperationTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }


    public static String getOperationDesc(String key) {
        for (FrameAgreementHistoryOperationTypeEnum sourceEnum : FrameAgreementHistoryOperationTypeEnum.values()) {
            if (key.equals(sourceEnum.getType())) {
                return sourceEnum.getDescription();
            }
        }
        return "-";
    }

}