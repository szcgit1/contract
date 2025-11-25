package com.xtm.contract.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.xtm.common.log.enums.ApiStateEnum;
import com.xtm.common.log.model.domain.ApiCategory;
import com.xtm.common.log.model.domain.ApiDirection;
import com.xtm.common.log.model.dto.ApiLog;
import com.xtm.common.log.service.IForeignApiLogBusinessService;
import com.xtm.common.model.Result;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.contract.model.dto.InternalMatchingApiLogDto;
import com.xtm.contract.service.InternalMatchingApiLogService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * 内部匹配API日志服务实现
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 11:49
 */
@Slf4j
@RequiredArgsConstructor
@Service(value = "internalMatchingApiLogService")
public class InternalMatchingApiLogServiceImpl implements InternalMatchingApiLogService {
    
    /**
     * 外部接口日志业务服务
     */
    private final IForeignApiLogBusinessService<InternalMatchingApiLog> iForeignApiLogBusinessService;
    
    /**
     * 接口类型 - 销售
     */
    private static final ApiCategory API_CATEGORY_SALE = ApiCategory.builder()
            .code(TmsContractConstant.ONE)
            .name("销售")
            .build();
    
    /**
     * 接口方向 - 关联
     */
    private static final ApiDirection API_DIRECTION_RELATED = ApiDirection.builder()
            .code("RELATED")
            .name("关联")
            .build();
    
    @Async
    @Override
    public void create(InternalMatchingApiLogDto dto) {
        log.info("====> 内部匹配日志 - 创建 - 传入参数: {} <====", dto);
        InternalMatchingApiLog apiLog = new InternalMatchingApiLog();
        apiLog.setCategory(API_CATEGORY_SALE.getCode());
        apiLog.setCategoryName(API_CATEGORY_SALE.getName());
        apiLog.setType(dto.getType().getCode());
        apiLog.setTypeName(dto.getType().getDescription());
        apiLog.setDirection(API_DIRECTION_RELATED.getCode());
        apiLog.setOperation(dto.getOperation().getCode());
        apiLog.setBizOp(API_DIRECTION_RELATED.getName() + dto.getOperation().getDescription());
        apiLog.setMatchedCode(dto.getMatchedCode());
        apiLog.setReferenceCode(dto.getReferenceCode());
        Result<?> result = dto.getResult();
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
            log.info("====> 内部匹配日志 - 保存报文日志成功 <====");
        } catch (Exception e) {
            log.error("====> 内部匹配日志 - 保存报文日志时发生异常 <====", e);
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
    protected static class InternalMatchingApiLog extends ApiLog implements Serializable {
        
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
         * 两厂采销 - 内部匹配 - 已匹配的编码
         */
        private String matchedCode;
        
        /**
         * 两厂采销 - 内部匹配 - 被匹配的编码
         */
        private String referenceCode;
        
    }
    
}
