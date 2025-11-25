package com.xtm.contract.service.impl;

import com.xtm.common.model.Result;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.convert.ThirdPartyContractConverter;
import com.xtm.contract.model.dto.BaseDto;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractDto;
import com.xtm.contract.model.dto.ThirdPartyFrameworkContractModifyStatusDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractGoodsQuantityDto;
import com.xtm.contract.model.dto.ThirdPartySalesContractModifyStatusDto;
import com.xtm.contract.model.param.NcFrameAgreementSaveParam;
import com.xtm.contract.model.param.NcFrameAgreementUpdateStateParam;
import com.xtm.contract.model.param.NcSalesContractAddOrUpdateParam;
import com.xtm.contract.model.param.NcUpdateStateParam;
import com.xtm.contract.model.param.UpdateAccumulateOrdersMainQuantityParam;
import com.xtm.contract.service.AbstractThirdPartyService;
import com.xtm.contract.service.FrameAgreementService;
import com.xtm.contract.service.SalesContractService;
import com.xtm.contract.service.ThirdPartyContractStrategy;
import com.xtm.setting.feign.SettingFeign;
import com.xtm.setting.model.vo.CargoOwnerInfoResult;
import com.xtm.utils.json.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * NC合同策略
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 09:58
 */
@Slf4j
@RequiredArgsConstructor
@Service(value = "ncContractStrategy")
public class NcContractStrategy extends AbstractThirdPartyService implements ThirdPartyContractStrategy {
    
    /**
     * 三方合同转换器
     */
    private final ThirdPartyContractConverter thirdPartyContractConverter;
    
    /**
     * 框架合同Service
     */
    private final FrameAgreementService frameAgreementService;
    
    /**
     * 销售合同Service
     */
    private final SalesContractService salesContractService;
    
    /**
     * tms-setting服务Feign接口
     */
    private final SettingFeign settingFeign;
    
    @Override
    public TmsContractConstant.ThirdPartySystemSource getSystemSource() {
        return TmsContractConstant.ThirdPartySystemSource.NC;
    }
    
    @Async
    @Override
    public void frameworkContractCreateOrUpdate(ThirdPartyFrameworkContractDto dto, boolean isCreate) {
        String action = isCreate ? "NC创建三方框架合同" : "NC修改三方框架合同";
        CompatibleApiLog apiLog = new CompatibleApiLog();
        apiLog.setUrl("/apiPlat/tms-contract/third-party/framework");
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(API_TYPE_FRAMEWORK_CONTRACT.getCode());
        apiLog.setTypeName(API_TYPE_FRAMEWORK_CONTRACT.getName());
        apiLog.setDirection(API_DIRECTION_IN.getCode());
        apiLog.setOperation(isCreate ? API_OPERATION_CREATE.getCode() : API_OPERATION_UPDATE.getCode());
        apiLog.setBizOp(dto.getManualUpdate() ? API_OPERATION_MANUAL_UPDATE.getName() : (isCreate ? API_DIRECTION_IN.getName() + API_OPERATION_CREATE.getName() : API_DIRECTION_IN.getName() + API_OPERATION_UPDATE.getName()));
        apiLog.setOuterCode(dto.getCode());
        ThirdPartyDtoWrapper<BaseDto> wrapper = ThirdPartyDtoWrapper.builder()
                .action(action)
                .dto(dto)
                .apiLog(apiLog)
                .build();
        this.submitBusinessLogic(wrapper, x -> {
            NcFrameAgreementSaveParam param = thirdPartyContractConverter.dto2Param((ThirdPartyFrameworkContractDto) x);
            param.setInterSource(isCreate ? TmsContractConstant.ZERO : TmsContractConstant.ONE);
            log.info("====> {} - 业务参数转换完成: {} <====", action, JsonUtils.toJSONString(param));
            // 查询基地来源
            Optional.ofNullable(settingFeign.getCargoOwnerByOwnerCode(param.getShippingMain()))
                    .filter(Result::isSuccess)
                    .map(Result::getData)
                    .map(CargoOwnerInfoResult::getBusiSource)
                    .map(Integer::valueOf)
                    .ifPresent(wrapper.getApiLog()::setBusiSource);
            return frameAgreementService.ncCreateOrUpdateAgreement(param);
        });
    }
    
    @Async
    @Override
    public void frameworkContractModifyStatus(ThirdPartyFrameworkContractModifyStatusDto dto) {
        String action = "NC更新三方框架合同状态";
        CompatibleApiLog apiLog = new CompatibleApiLog();
        apiLog.setUrl("/apiPlat/tms-contract/third-party/framework/status");
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(API_TYPE_FRAMEWORK_CONTRACT_STATUS.getCode());
        apiLog.setTypeName(API_TYPE_FRAMEWORK_CONTRACT_STATUS.getName());
        apiLog.setDirection(API_DIRECTION_IN.getCode());
        apiLog.setOperation(API_OPERATION_UPDATE.getCode());
        apiLog.setBizOp(API_DIRECTION_IN.getName() + API_OPERATION_UPDATE.getName());
        ThirdPartyDtoWrapper<BaseDto> wrapper = ThirdPartyDtoWrapper.builder()
                .action(action)
                .dto(dto)
                .apiLog(apiLog)
                .build();
        this.submitBusinessLogic(wrapper, x -> {
            NcFrameAgreementUpdateStateParam param = thirdPartyContractConverter.dto2Param((ThirdPartyFrameworkContractModifyStatusDto) x);
            log.info("====> {} - 业务参数转换完成: {} <====", action, JsonUtils.toJSONString(param));
            return frameAgreementService.ncBatchUpdateState(param);
        });
    }
    
    @Async
    @Override
    public void salesContractCreateOrUpdate(ThirdPartySalesContractDto dto, boolean isCreate) {
        String action = isCreate ? "NC创建三方销售合同" : "NC修改三方销售合同";
        CompatibleApiLog apiLog = new CompatibleApiLog();
        apiLog.setUrl("/apiPlat/tms-contract/third-party/sales");
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(API_TYPE_SALES_CONTRACT.getCode());
        apiLog.setTypeName(API_TYPE_SALES_CONTRACT.getName());
        apiLog.setDirection(API_DIRECTION_IN.getCode());
        apiLog.setOperation(isCreate ? API_OPERATION_CREATE.getCode() : API_OPERATION_UPDATE.getCode());
        apiLog.setBizOp(dto.getManualUpdate() ? API_OPERATION_MANUAL_UPDATE.getName() : (isCreate ? API_DIRECTION_IN.getName() + API_OPERATION_CREATE.getName() : API_DIRECTION_IN.getName() + API_OPERATION_UPDATE.getName()));
        apiLog.setOuterCode(dto.getContractCode());
        ThirdPartyDtoWrapper<BaseDto> wrapper = ThirdPartyDtoWrapper.builder()
                .action(action)
                .dto(dto)
                .apiLog(apiLog)
                .build();
        this.submitBusinessLogic(wrapper, x -> {
            NcSalesContractAddOrUpdateParam param = thirdPartyContractConverter.dto2Param((ThirdPartySalesContractDto) x);
            log.info("====> {} - 业务参数转换完成: {} <====", action, JsonUtils.toJSONString(param));
            // 查询基地来源
            this.checkoutBusiSource(param, wrapper.getApiLog());
            return isCreate ? salesContractService.ncCreate(param) : salesContractService.ncUpdate(param);
        });
    }
    
    @Async
    @Override
    public void salesContractModifyStatus(ThirdPartySalesContractModifyStatusDto dto) {
        String action = "NC更新三方销售合同状态";
        CompatibleApiLog apiLog = new CompatibleApiLog();
        apiLog.setUrl("/apiPlat/tms-contract/third-party/sales/status");
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(API_TYPE_SALES_CONTRACT_STATUS.getCode());
        apiLog.setTypeName(API_TYPE_SALES_CONTRACT_STATUS.getName());
        apiLog.setDirection(API_DIRECTION_IN.getCode());
        apiLog.setOperation(API_OPERATION_UPDATE.getCode());
        apiLog.setBizOp(API_DIRECTION_IN.getName() + API_OPERATION_UPDATE.getName());
        ThirdPartyDtoWrapper<BaseDto> wrapper = ThirdPartyDtoWrapper.builder()
                .action(action)
                .dto(dto)
                .apiLog(apiLog)
                .build();
        this.submitBusinessLogic(wrapper, x -> {
            NcUpdateStateParam param = thirdPartyContractConverter.dto2Param((ThirdPartySalesContractModifyStatusDto) x);
            log.info("====> {} - 业务参数转换完成: {} <====", action, JsonUtils.toJSONString(param));
            return salesContractService.ncUpdateState(param);
        });
    }
    
    @Async
    @Override
    public void salesContractUpdateGoodsQuantity(ThirdPartySalesContractGoodsQuantityDto dto) {
        String action = "NC更新三方销售合同货物数量";
        CompatibleApiLog apiLog = new CompatibleApiLog();
        apiLog.setUrl("/apiPlat/tms-contract/third-party/sales/quantity");
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(API_TYPE_SALES_CONTRACT_GOODS_QUANTITY.getCode());
        apiLog.setTypeName(API_TYPE_SALES_CONTRACT_GOODS_QUANTITY.getName());
        apiLog.setDirection(API_DIRECTION_IN.getCode());
        apiLog.setOperation(API_OPERATION_UPDATE.getCode());
        apiLog.setBizOp(API_DIRECTION_IN.getName() + API_OPERATION_UPDATE.getName());
        ThirdPartyDtoWrapper<BaseDto> wrapper = ThirdPartyDtoWrapper.builder()
                .action(action)
                .dto(dto)
                .apiLog(apiLog)
                .build();
        this.submitBusinessLogic(wrapper, x -> {
            UpdateAccumulateOrdersMainQuantityParam param = thirdPartyContractConverter.dto2Param((ThirdPartySalesContractGoodsQuantityDto) x);
            log.info("====> {} - 业务参数转换完成: {} <====", action, JsonUtils.toJSONString(param));
            return salesContractService.updateAccumulateOrdersMainQuantity(param);
        });
    }
    
    /**
     * 检出基地来源
     * <p>按照发运公司查找</p>
     *
     * @param param  NC销售合同添加修改参数
     * @param apiLog CompatibleApiLog
     */
    private void checkoutBusiSource(NcSalesContractAddOrUpdateParam param, CompatibleApiLog apiLog) {
        String shippingCompanyId = param.getSalesContractGoods().get(0).getShippingCompanyId();
        Optional.ofNullable(settingFeign.getCargoOwnerByOwnerCode(shippingCompanyId))
                .filter(Result::isSuccess)
                .map(Result::getData)
                .map(CargoOwnerInfoResult::getBusiSource)
                .map(Integer::valueOf)
                .ifPresent(x -> {
                    apiLog.setBusiSource(x);
                    log.info("====> 获取基地来源 - 使用发运公司: {} - 查询结果: {} <====", shippingCompanyId, apiLog.getBusiSource());
                });
    }
    
    @Override
    public boolean checkFrameworkContractProhibitedRepush(ThirdPartyFrameworkContractDto dto) {
        return frameAgreementService.queryDisabledByMainId(dto.getMainId());
    }
    
    @Override
    public boolean checkSalesContractProhibitedRepush(ThirdPartySalesContractDto dto) {
        Integer version = Optional.ofNullable(dto.getChanges())
                .flatMap(changes -> changes.stream()
                        .map(ThirdPartySalesContractDto.ContractChange::getVersion)
                        .filter(Objects::nonNull)
                        .max(Integer::compareTo))
                .orElse(null);
        return salesContractService.querySalesContractBySalesContractIdAndVersion(dto.getSalesContractId(), version);
    }
    
}
