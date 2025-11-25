package com.xtm.contract.model.vo.fdd;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 *@Author: 王磊
 *@CreateTime: 2023-11-02  17:48
 *@Description:
 *@title: Agreement
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Agreement {

    /**
     * 协议ID
     * 此处协议指认证时用户签署的CA数字证书服务协议和电子签服务协议
     */
    private String agreementIds;

    /**
     * 协议确认流水号
     * 此处协议指认证时用户签署的CA数字证书服务协议和电子签服务协议
     */
    private String confirmRecordIds;

}
