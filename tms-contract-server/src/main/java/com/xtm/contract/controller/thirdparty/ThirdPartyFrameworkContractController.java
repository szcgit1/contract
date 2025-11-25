package com.xtm.contract.controller.thirdparty;

import com.xtm.common.model.Result;
import com.xtm.contract.factory.ThirdPartyContractStrategyFactory;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractDto;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractModifyStatusDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 三方框架合同API
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-08-29 19:48
 */
@Slf4j
@RestController
@RequestMapping(value = "/third-party/framework")
@RequiredArgsConstructor
@Api(tags = "三方框架合同API")
public class ThirdPartyFrameworkContractController {
    
    /**
     * 三方合同策略工厂
     */
    private final ThirdPartyContractStrategyFactory thirdPartyContractStrategyFactory;
    
    /**
     * 创建三方框架合同
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PostMapping
    @ApiOperation(value = "创建三方框架合同", notes = "创建三方框架合同", httpMethod = "POST")
    public Result<?> create(@RequestBody ThirdPartyFrameworkContractDto dto) {
        thirdPartyContractStrategyFactory.getStrategy(dto.getSystemSource())
                .frameworkContractCreateOrUpdate(dto, true);
        return Result.ok();
    }
    
    /**
     * 修改三方框架合同
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PutMapping
    @ApiOperation(value = "修改三方框架合同", notes = "修改三方框架合同", httpMethod = "PUT")
    public Result<?> update(@RequestBody ThirdPartyFrameworkContractDto dto) {
        thirdPartyContractStrategyFactory.getStrategy(dto.getSystemSource())
                .frameworkContractCreateOrUpdate(dto, false);
        return Result.ok();
    }
    
    /**
     * 更新三方框架合同状态
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PutMapping(value = "/status")
    @ApiOperation(value = "更新三方框架合同状态", notes = "更新三方框架合同状态", httpMethod = "PUT")
    public Result<?> updateStatus(@RequestBody ThirdPartyFrameworkContractModifyStatusDto dto) {
        thirdPartyContractStrategyFactory.getStrategy(dto.getSystemSource())
                .frameworkContractModifyStatus(dto);
        return Result.ok();
    }
    
    /**
     * 创建三方框架合同重新推送
     *
     * @param dto 请求参数
     * @return {@link Result}
     */
    @PostMapping(value = "/repush")
    @ApiOperation(value = "创建三方框架合同重新推送", notes = "创建三方框架合同重新推送", httpMethod = "POST")
    public Result<?> repush(@RequestBody ThirdPartyFrameworkContractDto dto) {
        log.info("====> 创建三方框架合同重新推送 - 请求参数: {} <====", dto);
        if (thirdPartyContractStrategyFactory.getStrategy(dto.getSystemSource()).checkFrameworkContractProhibitedRepush(dto)) {
            return Result.error("框架合同当前已启用, 无法重新推送");
        }
        dto.setManualUpdate(Boolean.TRUE);
        thirdPartyContractStrategyFactory.getStrategy(dto.getSystemSource())
                .frameworkContractCreateOrUpdate(dto, true);
        return Result.ok();
    }
    
}
