package com.xtm.contract.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xtm.common.enums.ErrorCodeEnum;
import com.xtm.common.exception.BusinessException;
import com.xtm.common.log.enums.ApiStateEnum;
import com.xtm.common.log.model.domain.ApiCategory;
import com.xtm.common.log.model.domain.ApiDirection;
import com.xtm.common.log.model.domain.ApiOperation;
import com.xtm.common.log.model.domain.ApiType;
import com.xtm.common.log.model.dto.ApiLog;
import com.xtm.common.log.service.IForeignApiLogBusinessService;
import com.xtm.common.model.Result;
import com.xtm.contract.model.dto.BaseDto;
import com.xtm.utils.json.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.Serializable;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 三方抽象类
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 16:50
 */
@Slf4j
public abstract class AbstractThirdPartyService {
    
    /**
     * 校验器
     */
    @Autowired
    private Validator validator;
    
    /**
     * 外部接口日志业务服务
     */
    @Autowired
    private IForeignApiLogBusinessService<CompatibleApiLog> iForeignApiLogBusinessService;
    
    /**
     * 接口类型 - 销售
     */
    protected static final ApiCategory API_CATEGORY_SALE = ApiCategory.builder()
            .code(1)
            .name("销售")
            .build();
    
    /**
     * 接口类型明细 - 框架合同
     */
    protected static final ApiType API_TYPE_FRAMEWORK_CONTRACT = ApiType.builder()
            .code(440001)
            .name("框架合同")
            .build();
    
    /**
     * 接口类型明细 - 框架合同状态
     */
    protected static final ApiType API_TYPE_FRAMEWORK_CONTRACT_STATUS = ApiType.builder()
            .code(440002)
            .name("框架合同状态")
            .build();
    
    /**
     * 接口类型明细 - 销售合同
     */
    protected static final ApiType API_TYPE_SALES_CONTRACT = ApiType.builder()
            .code(440011)
            .name("销售合同")
            .build();
    
    /**
     * 接口类型明细 - 销售合同状态
     */
    protected static final ApiType API_TYPE_SALES_CONTRACT_STATUS = ApiType.builder()
            .code(440012)
            .name("销售合同状态")
            .build();
    
    /**
     * 接口类型明细 - 销售合同货物数量
     */
    protected static final ApiType API_TYPE_SALES_CONTRACT_GOODS_QUANTITY = ApiType.builder()
            .code(440013)
            .name("销售合同货物数量")
            .build();
    
    /**
     * 接口方向 - 接收
     */
    protected static final ApiDirection API_DIRECTION_IN = ApiDirection.builder()
            .code("IN")
            .name("接收")
            .build();
    
    /**
     * 接口操作 - 创建
     */
    protected static final ApiOperation API_OPERATION_CREATE = ApiOperation.builder()
            .code("I")
            .name("创建")
            .build();
    
    /**
     * 接口操作 - 更新
     */
    protected static final ApiOperation API_OPERATION_UPDATE = ApiOperation.builder()
            .code("U")
            .name("更新")
            .build();
    
    /**
     * 接口操作 - 人工更新
     */
    protected static final ApiOperation API_OPERATION_MANUAL_UPDATE = ApiOperation.builder()
            .code("MU")
            .name("人工更新")
            .build();
    
    /**
     * 通用DTO校验
     *
     * @param dto    请求参数
     * @param groups 校验分组
     * @param <T>    dto类型
     */
    public <T> void validateDto(T dto, Class<?>... groups) {
        if (null == dto) {
            throw new IllegalArgumentException("入参校验失败: 参数为空");
        }
        if (dto instanceof String) {
            if (StrUtil.isBlank((String) dto)) {
                throw new IllegalArgumentException("入参校验失败: 参数为空");
            }
            return;
        }
        Set<ConstraintViolation<T>> violationSet = validator.validate(dto, groups);
        if (CollUtil.isNotEmpty(violationSet)) {
            String errorMsg = violationSet.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(StrUtil.COMMA));
            throw new IllegalArgumentException("入参校验失败: " + errorMsg);
        }
    }
    
    /**
     * 执行业务逻辑
     *
     * @param wrapper       三方请求包装
     * @param businessLogic 业务逻辑
     * @param groups        校验组
     * @param <T>           DTO类型
     */
    protected <T extends BaseDto> void submitBusinessLogic(ThirdPartyDtoWrapper<T> wrapper, Function<T, Result<?>> businessLogic, Class<?>... groups) {
        String action = wrapper.getAction();
        T dto = wrapper.getDto();
        CompatibleApiLog apiLog = wrapper.getApiLog();
        log.info("====> {} - 请求参数: {} <====", wrapper.getAction(), dto);
        // 日志模块序列化使用FastJson
        apiLog.setParams(JSON.toJSONString(dto));
        Result<?> result = null;
        try {
            // 1. 入参校验
            this.validateDto(dto, groups);
            log.info("====> {} - 入参校验完成 <====", action);
            // 2. 执行业务逻辑
            result = businessLogic.apply(dto);
            if (null == result) {
                log.info("====> {} - 业务执行完成 <====", action);
                result = Result.ok();
            } else {
                log.info("====> {} - 业务执行完成 - 执行结果: {} <====", action, JsonUtils.toJSONString(result));
            }
        } catch (IllegalArgumentException | BusinessException be) {
            log.error("====> {} - 失败 - 业务异常 <====", action, be);
            if (null == result) {
                result = Result.error(ErrorCodeEnum.ERROR.getCode(), be.getMessage());
            }
        } catch (Exception e) {
            log.error("====> {} - 失败 - 系统异常 <====", action, e);
            if (null == result) {
                result = Result.error(ErrorCodeEnum.ERROR.getCode(), ErrorCodeEnum.SYSTEM_ERROR.getMsg());
            }
        }
        // 3. 保存日志
        // 日志模块序列化使用FastJson
        apiLog.setBizResp(JSON.toJSONString(result));
        apiLog.setBizCode(result.getCode().longValue());
        apiLog.setBizMsg(StrUtil.blankToDefault(result.getMsg(), result.getMessage()));
        ApiStateEnum status = result.isSuccess() ? ApiStateEnum.SUC : ApiStateEnum.ERR;
        apiLog.setStatus(status.name());
        apiLog.setBizClosedIfSuccess();
        apiLog.setStatusName(status.getName());
        try {
            iForeignApiLogBusinessService.addApiRequestLog(apiLog);
        } catch (Exception e) {
            log.error("====> {} - 保存报文日志时发生异常 <====", action, e);
        }
    }
    
    /**
     * 三方请求DTO包装类
     *
     * @param <T> 三方请求DTO
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    protected static class ThirdPartyDtoWrapper<T extends BaseDto> implements Serializable {
        
        /**
         * 操作描述
         */
        private String action;
        
        /**
         * 三方请求参数
         */
        private T dto;
        
        /**
         * API日志
         */
        private CompatibleApiLog apiLog;
        
        @Override
        public String toString() {
            return JsonUtils.toJSONString(this);
        }
        
    }
    
    /**
     * 兼容型日志参数
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    protected static class CompatibleApiLog extends ApiLog implements Serializable {
        
        /**
         * 基地来源
         * <br>
         * 0: 丰南
         * <br>
         * 1: 中铁
         * <br>
         * 2: 中重
         * <br>
         * 3: 其它
         * <br>
         * 4: 中铁翻车机房(受料槽)
         */
        private Integer busiSource;
        
        /**
         * 外部编号
         */
        private String outerCode;
        
    }
    
}
