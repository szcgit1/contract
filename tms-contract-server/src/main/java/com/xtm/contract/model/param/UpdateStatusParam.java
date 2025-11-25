package com.xtm.contract.model.param;

import com.xtm.contract.model.domain.SalesContract;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UpdateStatusParam {

    @ApiModelProperty(value = "销售合同集合")
    private SalesContract salesContract;

    @ApiModelProperty(value = "false:启用 true:禁用")
    private Boolean disabled;
}
