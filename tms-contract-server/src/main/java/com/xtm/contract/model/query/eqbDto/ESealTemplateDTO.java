package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "印章模板")
public class ESealTemplateDTO {
    @ApiModelProperty(value = "账户id")
    private String accountId;
    @ApiModelProperty(value = "印章id")
    private String sealId;
    @ApiModelProperty(value = "业务类型", required = true)
    private String businessType;
}
