package com.xtm.contract.feign.callback;

import com.xtm.common.model.Result;
import com.xtm.contract.feign.InternalMatchingApiLogFeign;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 内部匹配API日志Feign降级实现
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 14:09
 */
@Slf4j
@Component
public class InternalMatchingApiLogFeignFallBack implements InternalMatchingApiLogFeign {
    
    @Override
    public Result<?> create(InternalMatchingApiLogDto dto) {
        log.error("====> 创建内部匹配日志 - 调用失败 <====");
        return Result.error("创建内部匹配日志 - 调用失败");
    }
    
}
