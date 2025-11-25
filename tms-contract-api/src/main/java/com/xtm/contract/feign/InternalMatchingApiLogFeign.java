package com.xtm.contract.feign;

import com.xtm.common.model.Result;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.feign.callback.InternalMatchingApiLogFeignFallBack;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 内部匹配API日志Feign
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 14:06
 */
@FeignClient(value = TmsContractConstant.APPLICATION_NAME, contextId = "internalMatchingApiLogFeign", fallback = InternalMatchingApiLogFeignFallBack.class, path = "/apiPlat/tms-contract")
public interface InternalMatchingApiLogFeign {
    
    /**
     * 创建内部匹配日志
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PostMapping("/api-logs/internal-matching")
    Result<?> create(@RequestBody InternalMatchingApiLogDto dto);
    
}
