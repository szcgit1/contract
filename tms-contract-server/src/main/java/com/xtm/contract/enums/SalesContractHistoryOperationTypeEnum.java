package com.xtm.contract.enums;

import lombok.Getter;

/**
 * 业务历史操作-操作类型枚举;
 */
@Getter
public enum SalesContractHistoryOperationTypeEnum {

    CREATE("create", "创建"),
    NC_CREATE("ncCreate", "NC创建"),
    UPDATE("update", "更新"),
    UPDATE_STATE("updateState", "修改计划状态"),
    NC_UPDATE_STATE("ncUpdateState", "修改计划状态"),
    RE_FRAME_CONTRACT("reFrameContract", "关联框架合同"),
    CANCEL_RE_FRAME_CONTRACT("cancelReFrameContract", "取消关联框架合同"),

    ;
    private final String type;//修改来源;
    private final String description;//描述;

    SalesContractHistoryOperationTypeEnum(String type, String description) {
        this.type = type;
        this.description = description;
    }


    public static String getOperationDesc(String key) {
        for (SalesContractHistoryOperationTypeEnum sourceEnum : SalesContractHistoryOperationTypeEnum.values()) {
            if (key.equals(sourceEnum.getType())) {
                return sourceEnum.getDescription();
            }
        }
        return "-";
    }

}