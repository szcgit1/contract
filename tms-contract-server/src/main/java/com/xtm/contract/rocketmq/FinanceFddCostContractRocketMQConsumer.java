package com.xtm.contract.rocketmq;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.model.SysUser;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.ContractConstant;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.mapper.ContractMapper;
import com.xtm.contract.model.domain.FddContractDayCost;
import com.xtm.contract.model.domain.FddCostDetail;
import com.xtm.contract.model.mq.FinanceFddCostContract;
import com.xtm.contract.model.vo.contract.ContractInfoQryVO;
import com.xtm.contract.utils.DateUtil;
import com.xtm.user.model.vo.UserInfoVo;
import com.yomahub.tlog.core.mq.TLogMqConsumerProcessor;
import com.yomahub.tlog.core.mq.TLogMqRunner;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@RocketMQMessageListener(topic = "${rocketmq.topic-prefix}" + StrUtil.DASHED + RocketMqTopicConstant.FINANCE_FDD_CONTRACT_COST, selectorExpression = "${rocketmq.consumer.selector-expression}", consumerGroup = "${rocketmq.consumer.group}" + StrUtil.DASHED + RocketMqTopicConstant.FINANCE_FDD_CONTRACT_COST)
public class FinanceFddCostContractRocketMQConsumer implements RocketMQListener<TLogMqWrapBean<String>> {
    @Value(value = "${finance_fdd_contract_cost:0.02}")
    private String financeFddContractCost;
    @Value(value = "${finance_fdd_company_name:法大大}")
    private String companyName;
    @Value(value = "${finance_fdd_project_contract:合同签署}")
    private String projectContract;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private TmsUserService userService;
    @Autowired
    RedissonClient redissonClient;
    @Resource
    private NacosValueConfig nacosValueConfig;

    @Override
    public void onMessage(TLogMqWrapBean<String> tlog) {
        log.info("====>financeFddCostContractRocketMQConsumer MQ消费监听器 - 开始消费: {} <====", JSON.toJSONString(tlog));
        if (null == tlog) {
            log.error("====>financeFddCostContractRocketMQConsumer MQ消费监听器 - 消息解析失败 <====");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            TLogMqConsumerProcessor.process(tlog, (TLogMqRunner<String>) this::businessConsume);
            log.info("====>financeFddCostContractRocketMQConsumer MQ消费监听器 - 消费成功 <====");
        } catch (Exception e) {
            log.error("====>financeFddCostContractRocketMQConsumer MQ消费监听器 - 消费异常 <====", e);
        } finally {
            stopWatch.stop();
            log.info("====>financeFddCostContractRocketMQConsumer MQ消费监听器 - 消费完成, 耗时: {} 毫秒 <====", stopWatch.getLastTaskTimeMillis());
        }
    }

    /**
     * 实际业务处理
     *
     * @param json 消息体内容
     */
    private void businessConsume(String json) {
        FinanceFddCostContract message = JSONObject.toJavaObject(JSONObject.parseObject(json), FinanceFddCostContract.class);

        Query query = new Query();
        query.addCriteria(Criteria.where("busId").is(message.getBusId()));
        long count = mongoTemplate.count(query, FddCostDetail.class);
        if(count > 0){
            log.info("===================法大大自动签署合同计: 重复消费！========================");
            return;
        }

        ContractInfoQryVO contract = contractMapper.findContractById(message.getContractId());
        if(contract == null){
            return;
        }
        SysUser sessionUser = new SysUser();
        sessionUser.setId("1943483972360036354");
        sessionUser.setName("thirdpart");
        sessionUser.setCompanyId(contract.getCarryCompany().getId());
        UserInfoVo user;
        try {
            LoginUserContextHolder.setUser(sessionUser);
            user = userService.getUserByCompanyId(contract.getCarryCompany().getId());
        } finally {
            LoginUserContextHolder.clear();
        }
        if(user == null){
            return;
        }
        String key = this.nacosValueConfig.getPrefix() + "fdd_contract_cost_"+ message.getContractId();
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            calculate(user,message,contract);
        } finally {
            lock.unlock();
        }
    }

    private void calculate(UserInfoVo user,FinanceFddCostContract message,ContractInfoQryVO contract){
        FddCostDetail detail = new FddCostDetail();
        detail.setCallTime(message.getCallTIme());
        detail.setCost(financeFddContractCost);
        detail.setPhoneNum(user.getMobile());
        detail.setUserId(user.getId());
        detail.setUserName(user.getName());
        detail.setContractCode(contract.getContractCode());
        detail.setType(ContractConstant.costKeyword.contract);
        detail.setBusId(message.getBusId());
        detail.setCallMonth(DateUtil.day2String(new Date(detail.getCallTime()),"yyyy-MM"));
        detail.setCallDay(DateUtil.day2String(new Date(detail.getCallTime()),"yyyy-MM-dd"));
        detail.setCompanyName(companyName);
        detail.setProject(projectContract);
        mongoTemplate.save(detail);
        countByUserIdAndMonth(detail);
    }
    public void countByUserIdAndMonth(FddCostDetail detail) {
        String callDay = DateUtil.day2String(new Date(detail.getCallTime()), "yyyy-MM-dd");
        Query queryDetail = new Query();
        queryDetail.addCriteria(Criteria.where("callDay").is(callDay).and("type").is(ContractConstant.costKeyword.contract));
        Long callTimes = mongoTemplate.count(queryDetail,FddCostDetail.class);

        Query query = new Query();
        query.addCriteria(Criteria.where("callDay").is(callDay));
        FddContractDayCost cost = mongoTemplate.findOne(query, FddContractDayCost.class);

        if(cost == null){
            cost = new FddContractDayCost();
            cost.setCallTimes(callTimes.intValue());
            cost.setCreateTime(System.currentTimeMillis());
            cost.setCallDay(detail.getCallDay());
            cost.setUnitPrice(financeFddContractCost);
            cost.setCompanyName(companyName);
            cost.setProject(projectContract);
        }
        cost.setUpdateTime(System.currentTimeMillis());
        cost.setCallTimes(callTimes.intValue());
        cost.setCost(new BigDecimal(financeFddContractCost).multiply(BigDecimal.valueOf(cost.getCallTimes())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        mongoTemplate.save(cost);
    }

}
