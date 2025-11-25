package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

import java.util.List;

@Data
public class EPersonalAuthenticationInfoReq {
    /**指定默认认证类型*/
    private String authType;
    /**指定页面显示认证方式*/
    private List<String> availableAuthTypes;
    /**指定通过银行卡认证或运营商认证方式时，是否使用详情版，如指定则核验失败可返回具体不匹配信息，传空默认为普通版*/
    private List<String> authAdvancedEnabled;
    /**接收实名认证链接短信通知的手机号*/
    private String receiveUrlMobileNo;
    /**业务方交互上下文信息*/
    private EContextInfoReq contextInfo;
    /**个人实名认证的基本信息*/
    private EIndivInfoReq indivInfo;
    /**认证配置信息*/
    private EConfigParamsReq configParams;
    /**是否允许重复实名，默认允许*/
    private Boolean repeatIdentity;
}
