package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyVasParam {

    @ApiModelProperty(value = "增值业务Id")
    private String vasId;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "增值业务code")
    private Integer vasCode;

    @ApiModelProperty(value = "单价")
    private BigDecimal perPrice;

    @ApiModelProperty(value = "是否使用平台公司消费账户")
    private Boolean usePlatAccountStatus;

}
