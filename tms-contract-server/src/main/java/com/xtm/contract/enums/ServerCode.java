package com.xtm.contract.enums;

import com.xtm.contract.utils.StringUtils;

/**
 * @Author: Lu
 * @Description: 接口返回枚举值
 * @Date: 2021/2/26 11:28
 * @Version: 1.0
 */
public enum ServerCode {

    BANK("10", "银行服务"),
    CARGO("20", "货源服务"),
    CONTRACT("30", "合同服务"),
    NOTICE("40", "通知服务"),
    SUP("50", "监管服务"),
    COMPANY("60", "公司服务"),
    FILE("70", "文件服务"),
    TMS("80", "订单调度服务");

    private String code;
    private String message;

    private ServerCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public static void main(String[] args) {
        System.out.println(ServerCode.BANK.getMessage());

    }

    public static final String SYSTEM_CODE = "1";
    public static final String CHILDREN_SYSTEM_CODE = "10";

    public static final String DEFAULT_MODEL_CODE = "99";
    public static final String DEFAULT_ERROR_CODE = "9999";


    public static final int DEFAULT_MODEL_LENGTH = 2;
    public static final int DEFAULT_FUNCTION_LENGTH = 2;
    public static final int DEFAULT_ERROR_LENGTH = 4;

    public static final int FUNCTIONERRORCODE_SPLIT_LENGTH = 3;
    /**
     * errorCode封装规则
     * //13位：系统1+子系统2+服务2+模块2+功能2+预留4位
     * 1112024488556L;
     *
     * @param code      服务2位,用枚举
     * @param model     模块2位
     * @param function  功能2位
     * @param errorCode 预留4位
     * @return
     */
    public static Integer getServerCode(ServerCode code, String model, String function, String errorCode) {
        return Integer.valueOf(500);
    }

}
