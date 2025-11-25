package com.xtm.contract.config;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.model.SysUser;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.util.UriEncoder;

/**
 * 通用feign的设置信息
 *
 * @author zhang
 */
@Slf4j
@Component
public class FeignConfig implements RequestInterceptor {

    /**
     * feign 的头部信息
     */
    @Override
    public void apply (RequestTemplate requestTemplate) {
        try {
            String sessionJson = getSessionJson();
            requestTemplate.header("session", sessionJson);
            log.info("common.FeignConfig.session: {}", sessionJson);
        } catch (Exception e) {
            log.error("!!!!!!!!!!!!!!!common set feign head error!!!!!!!!!!!!!!!!", e);
        }
    }

    private String getSessionJson () {
        SysUser sessionInfo = LoginUserContextHolder.getUser();
        JSONObject json = JSONUtil.parseObj(sessionInfo, true);
        if (json.isEmpty()) {
            return "";
        }
        return UriEncoder.encode(json.toString());
    }

}
