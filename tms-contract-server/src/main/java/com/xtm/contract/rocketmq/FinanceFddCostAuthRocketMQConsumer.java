package com.xtm.contract.rocketmq;


import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xtm.common.context.LoginUserContextHolder;
import com.xtm.common.model.Result;
import com.xtm.common.model.SysUser;
import com.xtm.contract.feign.TmsUserService;
import com.xtm.contract.model.mq.FinanceFddCostAuth;
import com.xtm.contract.service.FddFeignService;
import com.xtm.contract.utils.DateUtil;
import com.xtm.contract.config.NacosValueConfig;
import com.xtm.contract.constant.ContractConstant;
import com.xtm.contract.model.domain.FddAuthMonthCost;
import com.xtm.contract.model.domain.FddCostDetail;
import com.xtm.thirdparty.auth.feign.FddElectricSealFeign;
import com.xtm.thirdparty.auth.model.param.QueryFddSealByKeyParam;
import com.xtm.thirdparty.auth.model.resp.FddElectricSealResp;
import com.xtm.user.model.vo.UserVo;
import com.xtm.utils.json.JsonUtils;
import com.yomahub.tlog.core.mq.TLogMqConsumerProcessor;
import com.yomahub.tlog.core.mq.TLogMqRunner;
import com.yomahub.tlog.core.mq.TLogMqWrapBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
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
@RocketMQMessageListener(topic = "${rocketmq.topic-prefix}" + StrUtil.DASHED + RocketMqTopicConstant.FINANCE_FDD_AUTH_COST, selectorExpression = "${rocketmq.consumer.selector-expression}", consumerGroup = "${rocketmq.consumer.group}" + StrUtil.DASHED + RocketMqTopicConstant.FINANCE_FDD_AUTH_COST)
public class FinanceFddCostAuthRocketMQConsumer implements RocketMQListener<TLogMqWrapBean<String>> {
    @Value(value = "${finance_fdd_auth_cost:0.35}")
    private String financeFddauthCost;
    @Value(value = "${finance_fdd_company_name:法大大}")
    private String companyName;
    @Value(value = "${finance_fdd_project_auth:个人三要素+补充险}")
    private String projectAuth;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private TmsUserService userService;
    @Autowired
    RedissonClient redissonClient;
    @Resource
    private NacosValueConfig nacosValueConfig;

    @Resource
    private FddElectricSealFeign fddElectricSealFeign;

    @Override
    public void onMessage(TLogMqWrapBean<String> tlog) {
        log.info("====>financeFddCostAuthRocketMQConsumer MQ消费监听器 - 开始消费: {} <====", JSON.toJSONString(tlog));
        if (null == tlog) {
            log.error("====>financeFddCostAuthRocketMQConsumer MQ消费监听器 - 消息解析失败 <====");
            return;
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        try {
            TLogMqConsumerProcessor.process(tlog, (TLogMqRunner<String>) this::businessConsume);
            log.info("====>financeFddCostAuthRocketMQConsumer MQ消费监听器 - 消费成功 <====");
        } catch (Exception e) {
            log.error("====>financeFddCostAuthRocketMQConsumer MQ消费监听器 - 消费异常 <====", e);
        } finally {
            stopWatch.stop();
            log.info("====>financeFddCostAuthRocketMQConsumer MQ消费监听器 - 消费完成, 耗时: {} 毫秒 <====", stopWatch.getLastTaskTimeMillis());
        }
    }

    /**
     * 实际业务处理
     *
     * @param json 消息体内容
     */
    private void businessConsume(String json) {
        FinanceFddCostAuth message = JSONObject.toJavaObject(JSONObject.parseObject(json), FinanceFddCostAuth.class);

        //防重复消费
        Query query = new Query();
        query.addCriteria(Criteria.where("busId").is(message.getBusId()));
        long count = mongoTemplate.count(query, FddCostDetail.class);
        if(count > 0){
            log.info("===================法大大实名认证统计: 重复消费！========================");
            return;
        }
        Query querySuccess = new Query();
        querySuccess.addCriteria(Criteria.where("userId").is(message.getBusId()).and("success").is(true));
        long succcess = mongoTemplate.count(querySuccess, FddCostDetail.class);
        if(succcess > 0){
            log.info("===================法大大实名认证统计: 已实名认证！========================");
            return;
        }

        //查询法大大信息
        QueryFddSealByKeyParam param = new QueryFddSealByKeyParam();
        param.setCustomerId(message.getCustomerId());
        Result<FddElectricSealResp> fddResult = fddElectricSealFeign.getFddElectricSeal(param);
        if(!fddResult.isSuccess()){
            log.error("===================法大大实名认证统计： 查询法大大电子签章异常，结果：{}",fddResult);
            return;
        }
        FddElectricSealResp fddElectricSeal = fddResult.getData();
        if(fddElectricSeal == null){
            log.error("===================法大大实名认证统计： 通过客户号未查询到电子签章，CustomerId：{}",message.getCustomerId());
            return;
        }
        //获取用户信息
        SysUser sessionUser = new SysUser();
        sessionUser.setId("1943483972360036354");
        sessionUser.setName("thirdpart");
        sessionUser.setCompanyId("id00soB892HbaAumeXXTM");
        UserVo user;
        try {
            LoginUserContextHolder.setUser(sessionUser);
            user = userService.getUserByIdCardNo(fddElectricSeal.getOpenId());
        } finally {
            LoginUserContextHolder.clear();
        }
        if(user == null){
            log.error("===================法大大实名认证统计： 通过OpenId未查询到用户，OpenId：{}",fddElectricSeal.getOpenId());
            return;
        }

        String key = this.nacosValueConfig.getPrefix() + "fdd_auth_cost_"+ message.getCustomerId();
        RLock lock = redissonClient.getLock(key);
        try {
            lock.lock(5, TimeUnit.SECONDS);
            calculate(message,user);
        } finally {
            lock.unlock();
        }
    }

    private void calculate(FinanceFddCostAuth message, UserVo user){
        FddCostDetail detail = new FddCostDetail();
        BeanUtils.copyProperties(message,detail);
        detail.setCost(financeFddauthCost);
        detail.setCallTime(message.getCallTIme());
        detail.setBusId(message.getBusId());
        detail.setUserId(user.getId());
        detail.setUserName(user.getName());
        detail.setPhoneNum(user.getMobile());
        detail.setType(ContractConstant.costKeyword.auth);
        detail.setCallMonth(DateUtil.day2String(new Date(detail.getCallTime()),"yyyy-MM"));
        detail.setCompanyName(companyName);
        detail.setProject(projectAuth);
        mongoTemplate.save(detail);
        countByUserIdAndMonth(detail);
    }
    public void countByUserIdAndMonth(FddCostDetail detail) {
        Query queryDetail = new Query();
        queryDetail.addCriteria(Criteria.where("userId").is(detail.getUserId())
                .and("callMonth").is(detail.getCallMonth()).and("type").is(ContractConstant.costKeyword.auth));
        Long callTimes = mongoTemplate.count(queryDetail,FddCostDetail.class);
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(detail.getUserId())
                .and("callMonth").is(detail.getCallMonth()));
        FddAuthMonthCost cost = mongoTemplate.findOne(query, FddAuthMonthCost.class);
        if(cost == null){
            cost = new FddAuthMonthCost();
            cost.setUserId(detail.getUserId());
            cost.setCallMonth(detail.getCallMonth());
            cost.setCreateTime(System.currentTimeMillis());
            cost.setPhoneNum(detail.getPhoneNum());
            cost.setUnitPrice(financeFddauthCost);
            cost.setUserName(detail.getUserName());
            cost.setCompanyName(companyName);
            cost.setProject(projectAuth);
        }
        cost.setUpdateTime(System.currentTimeMillis());
        cost.setCallTimes(callTimes.intValue());
        cost.setCost(new BigDecimal(financeFddauthCost).multiply(BigDecimal.valueOf(cost.getCallTimes())).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        mongoTemplate.save(cost);
    }

}

