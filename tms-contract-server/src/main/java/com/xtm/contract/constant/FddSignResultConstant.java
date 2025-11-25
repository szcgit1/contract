package com.xtm.contract.constant;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  13:53
 *@Description:
 *@title: FddSignResult
 */
public class FddSignResultConstant {

    /**
     * 成功
     */
    public static final Integer success = 1;

    /**
     * 成功 Long
     */
    public static final Long success_long = 1L;

    /**
     * 失败（有时候会带上具体原因）
     */
    public static final Integer error = 0;

    /**
     * 合同上传 成功
     */
    public static final Integer UPLOAD_PDF_TO_FDD_SUCCESS = 1000;

    /**
     * 自动签署 成功
     */
    public static final Integer FDD_EXT_SIGN_AUTO_SUCCESS = 1000;

    /**
     * 合同归档 成功
     */
    public static final Integer CONTRACT_FILING_SUCCESS = 1000;

    /**
     * tms保存认证状态  认证状态 已认证：1
     */
    public static final Integer VERIFY_STATUS_SUCCESS = 1;

    /**
     * tms保存认证状态  认证状态 未认证：0
     */
    public static final Integer VERIFY_STATUS_FAIL = 0;

    /**
     * tms保存授权状态  认证状态 已授权：1
     */
    public static final Integer AUTH_AUTO_SIGN_STATUS_SUCCESS = 1;

    /**
     * tms保存授权状态  认证状态 未授权：0
     */
    public static final Integer AUTH_AUTO_SIGN_STATUS_FAIL = 0;

}
