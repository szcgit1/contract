package com.xtm.contract.config;

import cn.hutool.core.util.StrUtil;

import com.xtm.common.exception.BusinessException;
import com.xtm.contract.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author Tenshin
 * @Date 2024-08-19 10:27
 * @Version 0.0.1
 * @Description EnvironmentConstant 用于获取配置文件中的配置信息
 */
@Component
@RefreshScope
public class EnvironmentConstant {
    
    /**
     * TMS相关中间件ENV环境前缀
     */
    private static String tmsPrefix;

    /**
     * topic 环境前缀
     */
    private static String topicPrefix;
    @Value(value = "${rocketmq.topic-prefix:}")
    private void setTopicPrefix(String topicPrefix) {
        EnvironmentConstant.topicPrefix = StrUtil.isBlank(topicPrefix) ? StrUtil.EMPTY : topicPrefix.toLowerCase();
    }
    
    /**
     * TMS相关中间件ENV环境前缀setter
     *
     * @param tmsPrefix 环境前缀
     */
    @Value(value = "${tms.prefix:}")
    private void setTmsPrefix(String tmsPrefix) {
        EnvironmentConstant.tmsPrefix = StrUtil.isBlank(tmsPrefix) ? StrUtil.EMPTY : tmsPrefix.toLowerCase();
    }
    
    /**
     * 获取TMS相关中间件ENV环境前缀
     *
     * @return 环境前缀
     */
    public static String tmsPrefix() {
        return EnvironmentConstant.tmsPrefix;
    }
    
    /**
     * 获取带环境前缀tag的topic
     *
     * @param topic topic
     * @return topicWithTag
     */
    public static String topicWithTag(String topic) {
        if (StrUtil.isBlank(topic)) {
            throw new BusinessException("Topic must not be null");
        }
        if (!StringUtils.isEmpty(topicPrefix)) {
            topic = topicPrefix + StrUtil.DASHED + topic;
        }
        if (!StringUtils.isEmpty(tmsPrefix)) {
            topic = topic + StrUtil.COLON +tmsPrefix;
        }
        return topic;

    }
    
    /**
     * 获取带环境前缀的redis key
     *
     * @return redisPrefix
     */
    public static String redisPrefix() {
        return EnvironmentConstant.tmsPrefix.isEmpty() ? StrUtil.EMPTY : EnvironmentConstant.tmsPrefix + StrUtil.COLON;
    }
    
}
