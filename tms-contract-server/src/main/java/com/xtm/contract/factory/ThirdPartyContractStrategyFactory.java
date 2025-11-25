package com.xtm.contract.factory;

import cn.hutool.core.collection.CollUtil;
import com.xtm.common.exception.BusinessException;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.service.ThirdPartyContractStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 三方合同策略工厂
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 10:16
 */
@Slf4j
@RequiredArgsConstructor
@Component(value = "thirdPartyContractStrategyFactory")
public class ThirdPartyContractStrategyFactory implements InitializingBean {
    
    /**
     * 策略列表
     */
    private final List<ThirdPartyContractStrategy> strategyList;
    
    /**
     * 策略映射
     */
    private final Map<TmsContractConstant.ThirdPartySystemSource, ThirdPartyContractStrategy> strategyMap = new EnumMap<>(TmsContractConstant.ThirdPartySystemSource.class);
    
    
    /**
     * 获取策略
     *
     * @param systemSource 系统来源
     * @return 策略
     */
    public ThirdPartyContractStrategy getStrategy(TmsContractConstant.ThirdPartySystemSource systemSource) {
        return Optional.ofNullable(strategyMap.get(systemSource))
                .orElseThrow(() -> new BusinessException("未知三方合同策略 - 系统编码: " + systemSource));
    }
    
    @Override
    public void afterPropertiesSet() {
        if (CollUtil.isEmpty(strategyList)) {
            if (log.isWarnEnabled()) {
                log.warn("====> 三方合同策略实现为空 <====");
            }
            return;
        }
        strategyList.forEach(strategy -> strategyMap.put(strategy.getSystemSource(), strategy));
    }
    
}
