package com.xtm.contract.config;

import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;
@Data
@Component
@RefreshScope
public class NacosValueConfig {

    @Value("${contract.tjzwCardNo}")
    private String tjzwCardNo;
//    @Value("${contract.xituoCardNo}")
//    private String xituoCardNo;
//    @Value("${contract.fddNotifyUrl}")
    private String fddNotifyUrl;
//    @Value("${contract.fddMobile:19931650589}")
//    private String mobile;
    @Value("${contract.agentCode:zhgt}")
    private String agentCode;

    /**
     * 小铁马天津身份证号
     */
    @Value("${xtm.tj.cardNo}")
    private String xtmTjCardNo;

    /**
     * 小铁马天津公司id
     */
    @Value("${xtm.tj.companyId}")
    private String xtmTjCompanyId;

    /**
     * 小铁马甘肃身份证号
     */
    @Value("${xtm.gs.cardNo}")
    private String xtmGsCardNo;

    /**
     * 小铁马甘肃公司id
     */
    @Value("${xtm.gs.companyId}")
    private String xtmGsCompanyId;

    // 环境前缀
    @Value("${tms.prefix:}")
    private String prefix;

    public String getPrefix() {
        // 不空的话，拼接冒号
        if (StrUtil.isNotBlank(prefix)) {
            return prefix + StrUtil.COLON;
        }
        return prefix;
    }

}
