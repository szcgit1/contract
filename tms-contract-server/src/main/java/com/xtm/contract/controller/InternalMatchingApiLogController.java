package com.xtm.contract.controller;

import com.xtm.common.model.Result;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import com.xtm.contract.service.InternalMatchingApiLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 内部匹配日志API
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 11:41
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api-logs/internal-matching")
@Api(tags = "内部匹配日志API")
public class InternalMatchingApiLogController {
    
    /**
     * 内部匹配API日志服务
     */
    private final InternalMatchingApiLogService internalMatchingApiLogService;
    
    /**
     * 创建内部匹配日志
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PostMapping
    @ApiOperation(value = "创建内部匹配日志", notes = "创建内部匹配日志", httpMethod = "POST")
    public Result<?> create(@RequestBody @Validated InternalMatchingApiLogDto dto) {
        internalMatchingApiLogService.create(dto);
        return Result.ok();
    }
    
}
