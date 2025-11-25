package com.xtm.contract.model.enums;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/8 10:48
 * @desc
 */
public enum ContractErrorCode {
    /*--通用增删查改-- */
    DELETE_CONTRACT_NOT_EXIST("1001", "要删除的合同不存在"),
    DELETE_CONTRACT_ERROR("1002", "删除合同失败"),
    ADD_CONTRACT_ERROR("1003", "新增合同失败"),
    CONTRACT_DATA_ISNULL("1004","合同信息不存在"),
    CARRIOR_NOT_EXIST("1005", "当前合同的承运方不存在"),
    SAME_COMPANY_NO_CONTRACT("1006", "同一公司的业务不生成合同"),

    DELETE_CONTRACT_SUCCESS("1007", "删除成功"),
    ADD_CONTRACT_SUCCESS("1008", "保存成功"),
    ENABLE_TEMPLATE_SUCCESS("1009", "启用成功"),
    DISABLE_TEMPLATE_SUCCESS("1010", "禁用成功"),
    /* --补充合同-- */
    INVALID_CONTRACT_NO_SUPPLEMENT("1501", "已作废或已删除的单子不能生成补充合同"),


    /*--服务调用异常--*/
    VALUE_SERVICE_ERROR("1901","订阅服务调用异常"),
    DOCUMENT_SERVICE_ERROR("1902","单据服务调用异常"),
    FILE_SERVICE_ERROR("1903","文件服务调用异常"),
    COMPANY_SERVICE_ERROR("1904","公司服务调用异常"),

    /* --电子签署-- */
    NO_FOUND_EQBCONFIG("1101", "没有e签宝配置信息"),
    NO_EQBAUTH("1102", "当前平台在系统未授权e签宝，请联系管理员授权"),
    BALANCE_NOT_ENOUGH("1103","平台账户余额已不足，请及时充值"),
    LOCAL_HTML_ERROR("1201","LOCAL_HTML生成错误"),
    LOCAL_PDF_ERROR("1202","LOCAL_PFD生成错误"),
    FILE_DOWNLOAD_FAIL("1203","网络文件下载失败，请稍后再试"),
    FILE_UPLOAD_FAIL("1204","文件上传失败，文件服务异常"),
    ADMIN_NOT_NULL("1400","法人/管理员不能为空"),
    ADMIN_IDCARD_NOT_NULL("1401","法人/管理员身份证号不能为空"),//企业
    ADMIN_IDCARD_FORMAT_ERROR("1402","法人/管理员身份证号格式不正确"),
    SOCIALCREDITCODE_NOT_NULL("1403","统一社会信用码不能为空"),
    SOCIALCREDITCODE_FORMAT_ERROR("1404","统一社会信用码格式不正确"),

    DRIVER_IDCARD_NOT_NULL("1405","身份证号不能为空"),//个人
    DRIVER_IDCARD_FORMAT_ERROR("1406","身份证号格式不正确"),
    PHONE_NOT_NULL("140007","手机号不能为空"),

    TITLE_FORMAT_ERROR("1408","合同主题格式不正确"),
    PROCESS_NOT_ARCHIVED("1409","合同正在签署中，请稍后查看"),

    TRANSFER_UNLOAD_CONTRACT_NOT_SIGN("1410","中转卸货来源的运单合同，不需要签署"),

    XIAONIU_CONTRACT_BACK_ERROR("9999","签署失败"),
    XIAONIU_CONTRACT_BACK_SUCCESS("0000","签署成功"),

    TEMPLATE_NOT_EXITS("20001","合同模板不存在"),
    TEMPLATE_ADD_FAILURE("20002","合同模板新增失败");

    private String code;
    private String message;

    private ContractErrorCode(String code, String message) {
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
