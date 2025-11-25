package com.xtm.contract.model.query.eqbReq;

import lombok.Data;

@Data
public class EContextInfoReq {
    /**发起方业务上下文标识在异步通知时发送回发起方*/
    private String contextId;
    /**发起方接收实名认证状态变更通知的地址*/
    private String notifyUrl;
    /**认证发起来源*/
    private String origin;
    /**认证结束后页面跳转地址*/
    private String redirectUrl;
    /**认证完成是否显示结果页,默认显示*/
    private Boolean showResultPage;
}
