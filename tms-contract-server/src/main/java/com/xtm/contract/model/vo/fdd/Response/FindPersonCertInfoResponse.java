package com.xtm.contract.model.vo.fdd.Response;

import com.xtm.contract.model.vo.fdd.Agreement;
import com.xtm.contract.model.vo.fdd.Person;
import com.xtm.contract.model.vo.fdd.WebankOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  17:27
 *@Description: 查询个人实名认证信息响应报文
 *@title: FindPersonCertInfoResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPersonCertInfoResponse {

    /**
     * 交易号 该参数等同实名认证异步回调serialNO
     */
    private String transactionNo;

    /**
     * 1.个人
     * 2企业
     */
    private String type;

    /**
     * 审核通过时间   yyyy-MM-dd HH:mm:ss.0
     */
    private String passTime;

    /**
     * 认证提交时间   yyyy-MM-dd HH:mm:ss.0
     */
    private String authenticationSubmitTime;

    /**
     * 腾讯云刷脸时有值
     */
    private WebankOrder webankOrder;

    /**
     * 个人信息
     */
    private Person person;

    /**
     * 协议信息
     */
    private Agreement agreement;

}
