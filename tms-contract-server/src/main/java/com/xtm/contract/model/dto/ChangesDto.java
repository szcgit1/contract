package com.xtm.contract.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * 调度历史-业务变更信息;
 *
 * @author miaoyouhu
 * @date 2025/2/26 14:58
 */
@Data
@Builder
@Accessors(chain = true)
@FieldNameConstants
@NoArgsConstructor
@AllArgsConstructor
public class ChangesDto {

    private String before;//变更前的内容，内容格式为json,只保存变更的内容,参见设计文档
    private String after;//变更后的内容，内容格式为json,只保存变更的内容，参见设计文档;

}