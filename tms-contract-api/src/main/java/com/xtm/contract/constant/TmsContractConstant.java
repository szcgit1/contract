package com.xtm.contract.constant;

import com.alibaba.fastjson.serializer.JSONSerializable;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.xtm.common.exception.BusinessException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * TMS合同服务常量
 * <br>
 * 当前项目已有ContractConstant类了,换个名字
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 09:13
 */
public interface TmsContractConstant {
    
    /**
     * 服务名称
     */
    String APPLICATION_NAME = "tms-contract";
    
    /**
     * 0
     */
    Integer ZERO = 0;
    
    /**
     * 常量 - "0"
     */
    String ZERO_STR = "0";
    
    /**
     * 1
     */
    Integer ONE = 1;
    
    /**
     * 常量 - "1"
     */
    String ONE_STR = "1";
    
    /**
     * 2
     */
    Integer TWO = 2;
    
    /**
     * 常量 - "2"
     */
    String TWO_STR = "2";
    
    /**
     * NO
     */
    Integer NO = 0;
    
    /**
     * YES
     */
    Integer YES = 1;
    
    /**
     * 默认分页大小
     */
    Long DEFAULT_PAGE_SIZE = 20L;
    
    /**
     * MyBatis Plus limit 1
     */
    String MP_LIMIT_ONE = " LIMIT 1";
    
    /**
     * 公共线程池名称
     */
    String COMMON_THREAD_POOL_NAME = "asyncExecutor";
    
    
    /**
     * 三方系统来源
     */
    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum ThirdPartySystemSource implements JSONSerializable {
        
        /**
         * TMS
         */
        TMS(0, "TMS"),
        
        /**
         * NC
         */
        NC(1, "NC"),
        
        /**
         * 新网
         */
        XW(2, "新网");
        
        /**
         * 三方系统来源编码
         */
        @JsonValue
        private final Integer code;
        
        /**
         * 三方系统来源描述
         */
        private final String description;
        
        /**
         * 编码映射
         */
        private static final Map<Integer, ThirdPartySystemSource> MAP = Stream.of(values())
                .collect(Collectors.toMap(ThirdPartySystemSource::getCode, Function.identity()));
        
        /**
         * 根据三方系统来源编码获取三方系统来源
         *
         * @param code 三方系统来源编码
         * @return 三方系统来源
         */
        @JsonCreator
        public static ThirdPartySystemSource of(Integer code) {
            return Optional.ofNullable(MAP.get(code))
                    .orElseThrow(() -> new BusinessException("未知系统来源"));
        }
        
        @Override
        public void write(JSONSerializer serializer, Object fieldName, Type fieldType, int features) {
            if (null == code) {
                serializer.writeNull();
            } else {
                serializer.write(code);
            }
        }
        
    }
    
    /**
     * 内部匹配日志类型
     */
    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum InternalMatchingType {
        
        /**
         * 框架合同
         */
        FRAMEWORK_CONTRACT(550001, "框架合同", "内部匹配 - 框架合同"),
        
        /**
         * 销售合同
         */
        SALES_CONTRACT(550002, "销售合同", "内部匹配 - 销售合同"),
        
        /**
         * 销售订单
         */
        SALES_ORDER(550003, "销售订单", "内部匹配 - 销售订单"),
        
        /**
         * 销售订单转库单
         */
        SALES_ORDER_TRANSFER(550004, "销售订单转库单", "内部匹配 - 销售订单转库单"),
        
        /**
         * 订单匹配
         */
        ORDER_MATCHING(550005, "订单匹配", "内部匹配 - 订单匹配"),
        ;
        
        /**
         * 内部匹配类型编码
         */
        @JsonValue
        private final Integer code;
        
        /**
         * 日志类型描述
         */
        private final String description;
        
        /**
         * 日志类型描述2
         */
        private final String description2;
        
        /**
         * 编码映射
         */
        private static final Map<Integer, InternalMatchingType> MAP = Stream.of(values())
                .collect(Collectors.toMap(InternalMatchingType::getCode, Function.identity()));
        
        /**
         * 根据内部匹配类型编码获取内部匹配类型
         *
         * @param code 内部匹配类型编码
         * @return 内部匹配类型
         * @throws BusinessException 业务异常
         */
        @JsonCreator
        public static InternalMatchingType of(Integer code) {
            return Optional.ofNullable(MAP.get(code))
                    .orElseThrow(() -> new BusinessException("未知内部匹配接口类型"));
        }
        
    }
    
    /**
     * 内部匹配日志操作类型
     */
    @Getter
    @ToString
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    enum InternalMatchingOperation {
        
        /**
         * 协议
         */
        AGREEMENT("AGREEMENT", "协议"),
        
        /**
         * 订单
         */
        ORDER("ORDER", "订单"),
        
        /**
         * 合同
         */
        CONTRACT("CONTRACT", "合同"),
        
        /**
         * 基础信息
         */
        BASIC_INFO("BASIC_INFO", "基础信息"),
        ;
        
        /**
         * 内部匹配日志操作类型编码
         */
        @JsonValue
        private final String code;
        
        /**
         * 内部匹配日志操作类型描述
         */
        private final String description;
        
        /**
         * 编码映射
         */
        private static final Map<String, InternalMatchingOperation> MAP = Stream.of(values())
                .collect(Collectors.toMap(InternalMatchingOperation::getCode, Function.identity()));
        
        /**
         * 根据内部匹配操作类型编码获取内部匹配操作类型
         *
         * @param code 内部匹配操作类型编码
         * @return 内部匹配操作类型
         * @throws BusinessException 业务异常
         */
        @JsonCreator
        public static InternalMatchingOperation of(String code) {
            return Optional.ofNullable(MAP.get(code))
                    .orElseThrow(() -> new BusinessException("未知内部匹配接口操作类型"));
        }
        
    }
    
}
