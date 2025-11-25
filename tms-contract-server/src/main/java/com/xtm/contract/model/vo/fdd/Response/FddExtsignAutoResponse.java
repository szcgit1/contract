package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  21:18
 *@Description:
 *@title: fddExtsignAutoResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FddExtsignAutoResponse {

    /**
     * 处理结果
     * success：成功
     * error：失败
     */
    private String result;

    /**
     * 状态码
     * 1000：操作成功
     * 2001：参数缺失或者不合法
     * 2002：业务异常，失败原因见msg
     * 2003：其他错误，请联系法大大
     */
    private Integer code;

    /**
     * 下载合同链接
     */
    private String download_url;

    /**
     * 查看合同链接
     */
    private String viewpdf_url;

    /**
     * 描述
     */
    private String msg;

    /**
     * 自动签署交易号  (此字段不是法大大响应数据，是用来tms生成的)
     */
    private String extsignAutoTransId;
    /**
     * 短信发送链接
     */
    private String signUrl;

}
