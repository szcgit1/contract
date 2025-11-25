package com.xtm.contract.config;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;

@Configuration
@RefreshScope
@Data
@Slf4j
public class TmsContractConfig {

    @Value("${tms.contract.select.enable:true}")
    private Boolean contractSelectEnable;

    @Value("${tms.contract.update.enable:true}")
    private Boolean contractUpdateEnable;

    @Value("${tms.prefix:}")
    private String tmsPrefix;

    private final String TMS_CONTRACT_SELECT_REDIS_KEY = "tms:contract:select:enable";

    private final String TMS_CONTRACT_UPDATE_REDIS_KEY = "tms:contract:update:enable";

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @ApiOperation(value = "获取查询开关状态")
    public Boolean isContractSelectEnable() {
        String redisKey = getPrefix() + TMS_CONTRACT_SELECT_REDIS_KEY;
        String value = redisTemplate.opsForValue().get(redisKey);
        log.info("{},{}",redisKey, value);
        return StringUtils.isEmpty(value)|| "1".equals(value);
    }

    @ApiOperation(value = "获取更新开关状态")
    public Boolean isContractUpdateEnable() {
        String redisKey = getPrefix() + TMS_CONTRACT_UPDATE_REDIS_KEY;
        String value = redisTemplate.opsForValue().get(redisKey);
        log.info("{},{}",redisKey, value);
        return StringUtils.isEmpty(value)||"1".equals(value);
    }

    /**
     * 获取redis前缀
     * @return
     */
    public String getPrefix() {
        return StrUtil.isBlank(tmsPrefix) ? StrUtil.EMPTY : tmsPrefix+ StrUtil.COLON;
    }
}
