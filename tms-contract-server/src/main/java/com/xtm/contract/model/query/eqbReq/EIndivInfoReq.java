package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

/**
* @Description:    个人实名认证的基本信息
* @Author:         mxr
* @CreateDate:     2021-03-05 11:33
*/
@Data
public class EIndivInfoReq {
    /**个人银行卡号*/
    private String bankCardNo;
    /**个人证件号*/
    private String certNo;
    /**个人证件类型*/
    private String certType;
    private String mobileNo;
    private String name;
}
