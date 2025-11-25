package com.xtm.contract.utils;

import cn.hutool.core.util.StrUtil;
import com.xtm.contract.config.EnvironmentConstant;
import com.xtm.mq.common.model.MqResult;
import com.xtm.mq.common.service.MqTemplate;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RocketMqSendUtil {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public void sendMsg(String topic, String msg) {
        log.info("====> 准备发送至MQ - 原始Topic: {}, 消息参数为: {} <====", topic, msg);
        if (StrUtil.isBlank(topic)) {
            log.error("====> Topic为空, 发送失败 <====");
            return;
        }
        if (StrUtil.isBlank(msg)) {
            log.error("====> 消息为空, 发送失败 <====");
            return;
        }
        // Topic添加环境前缀tag
        String topicWithTag = EnvironmentConstant.topicWithTag(topic);
        // TLog
        TLogMqWrapBean<String> tLogMqWrapBean = new TLogMqWrapBean<>(msg);
        // 发送
        try {
            SendResult sendResult = rocketMQTemplate.syncSend(topicWithTag, tLogMqWrapBean);
            log.info("====> 发送至MQ成功, MessageId: {}, Topic: {}, Message: {},  <====", sendResult.getMsgId(), topicWithTag, msg);
        } catch (Exception e) {
            log.error("====> 发送至MQ失败, Topic: {}, Message: {} <====", topicWithTag, msg, e);
            throw e;
        }
    }
}
