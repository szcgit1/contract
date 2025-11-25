package com.xtm.contract.model.vo.fdd.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  20:34
 *@Description: 合同归档返回报文
 *@title: UploadPdfToFddResponse
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractFilingResponse {

    /**
     * 处理结果
     * success：成功
     * error：失败
     **/
    private String result;

    /**
     * 状态码
     * 1000：操作成功
     * 2001：参数缺失或者不合法
     * 2002：业务异常，失败原因见msg
     * 2003：其他错误，请联系法大大
     **/
    private Integer code;

    /**
     * 描述
     **/
    private String msg;

}
