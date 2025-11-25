package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BaseIn {
    @ApiModelProperty(value = "平台ID" , required = true)
    public String agentCode;
    @ApiModelProperty(value = "上传目录" , required = true)
    public String source;
}
