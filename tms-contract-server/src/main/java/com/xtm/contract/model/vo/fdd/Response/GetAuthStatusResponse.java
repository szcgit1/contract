package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  21:14
 *@Description:
 *@title: GetAuthStatusResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAuthStatusResponse {

    /**
     *
     * 授权类型
     * 1：接口线上授权：调用授权自动签接口完成的授权
     */
    private String authType;
    /**
     * 授权合同编号
     */
    private String contractId;
    /**
     * 授权通知邮箱或手机号
     */
    private String email;
    /**
     * 授权状态
     * 1：已授权
     * 0：未授权
     */
    private String status;
    /**
     * 授权流水号    接口线上授权时才返回
     */
    private String transactionId;

}
