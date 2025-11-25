package com.xtm.contract.service;

import com.xtm.contract.model.dto.InternalMatchingApiLogDto;

/**
 * 内部匹配API日志服务
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 11:48
 */
public interface InternalMatchingApiLogService {
    
    /**
     * 保存内部匹配API日志
     *
     * @param dto 请求参数
     */
    void create(InternalMatchingApiLogDto dto);
    
}
