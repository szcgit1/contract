package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  15:37
 *@Description: 法大大获取实名认证地址 响应报文
 *@title: GetVerifyUrlResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetVerifyUrlResponse {

    /**
     * 交易号（需要保存，用于证书申请和实名认证查询）
     */
    private String transactionNo;

    /**
     * 地址（需要保存，遇到中途退出认证或页面过期等情况可重新访问）  需要Base64解码
     */
    private String url;

    /**
     * 返回身份证信息，如果身份证为空，提示用户填写身份证  0:空
     */
    private String idcardNo;
}
