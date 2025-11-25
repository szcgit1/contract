package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 转换pdf入参
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToPdfIn {
    @ApiModelProperty(value = "pdf内容")
    public String content;

    @ApiModelProperty(value = "平台Code")
    public String agentCode;

    @ApiModelProperty(value = "上传目录")
    public String source;
}
