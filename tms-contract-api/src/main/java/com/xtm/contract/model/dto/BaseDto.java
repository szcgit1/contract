package com.xtm.contract.model.dto;

import cn.hutool.json.JSONUtil;
import io.swagger.annotations.ApiModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * 基础DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-08-29 15:29
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "BaseDto", description = "基础DTO")
public class BaseDto implements Serializable {
    
    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }
    
}
