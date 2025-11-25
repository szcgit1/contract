package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-03  10:27
 *@Description: 查询用户签署结果接口 返回报文
 *@title: QuerySignResultResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuerySignResultResponse {

    /**
     * 签署时间
     */
    private String endTime;

    /**
     * 签署状态
     * 3000:签章成功
     * 3001:签章失败
     * 3002:已撤销
     * 3003:已拒签
     * 9999:待签署
     */
    private String result;

    /**
     * 签章状态描述
     */
    private String result_desc;

    /**
     * 合同下载地址
     */
    private String download_url;

    /**
     * 合同查看地址
     */
    private String view_url;

    /**
     * 返回身份证信息，如果身份证为空，提示用户填写身份证  0:空
     */
    private String idcardNo;

}
