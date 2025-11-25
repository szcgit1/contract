package com.xtm.contract.model.vo.eqb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqbConfigInfo implements Serializable {
    /** 宿主 */
    private String host;

    private String appId;

    private String secret;

    /**异步通知地址*/
    private String notifyUrl;

    /**app跳转地址*/
    private String appRedirectUrl;

    /**pc端跳转地址*/
    private String pcRedirectUrl;
}
