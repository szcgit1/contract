package com.xtm.contract.model.dto;

import com.xtm.common.model.Result;
import com.xtm.contract.constant.TmsContractConstant;
import com.xtm.utils.json.JsonUtils;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 内部匹配API日志DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-10-10 11:27
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ApiModel(value = "InternalMatchingApiLogDto", description = "内部匹配API日志DTO")
public class InternalMatchingApiLogDto implements Serializable {
    
    /**
     * 内部匹配类型
     */
    @NotNull(message = "内部匹配类型不能为空")
    private TmsContractConstant.InternalMatchingType type;
    
    /**
     * 内部匹配操作类型
     */
    @NotNull(message = "内部匹配操作类型不能为空")
    private TmsContractConstant.InternalMatchingOperation operation;
    
    /**
     * 两厂采销 - 内部匹配 - 已匹配的编码
     */
    @NotBlank(message = "已匹配的编码不能为空")
    private String matchedCode;
    
    /**
     * 两厂采销 - 内部匹配 - 被匹配的编码
     */
    @NotBlank(message = "被匹配的编码不能为空")
    private String referenceCode;
    
    /**
     * 匹配结果
     */
    @NotNull(message = "匹配结果不能为空")
    private Result<?> result;
    
    @Override
    public String toString() {
        return JsonUtils.toJSONString(this);
    }
    
}
