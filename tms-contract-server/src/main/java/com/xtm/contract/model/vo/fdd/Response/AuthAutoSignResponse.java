package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  20:08
 *@Description: 获取授权自动签页面接口响应报文
 *@title: AuthAutoSignResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthAutoSignResponse {

    /**
     * 授权自动签交易Id
     */
    private String authAutoSignTransId;

    /**
     * 授权自动签合同id
     */
    private String authAutoSignContractId;

    /**
     * 授权自动签页面url
     */
    private String authAutoSignUrl;

    /**
     * 返回身份证信息，如果身份证为空，提示用户填写身份证  0:空
     */
    private String idcardNo;
}
