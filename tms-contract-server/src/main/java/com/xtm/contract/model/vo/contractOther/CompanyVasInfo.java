package com.xtm.contract.model.vo.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CompanyVasInfo {

    @ApiModelProperty(value = "发行公司Id")
    private String issuerCompanyId;

    @ApiModelProperty(value = "关联消费账户Id")
    private String consumeAccountId;

    @ApiModelProperty(value = "增值业务可用余额")
    private BigDecimal balance;

    @ApiModelProperty(value = "业务名称")
    private String vasName;

    @ApiModelProperty(value = "业务描述")
    private String vasDesc;

    @ApiModelProperty(value = "佣金类型")
    private Integer commissionType;

    @ApiModelProperty(value = "平台启用状态")
    private Boolean enabledStatus;

    @ApiModelProperty(value = "授权状态")
    private Boolean authorizedStatus;

    @ApiModelProperty(value = "全网业务模式")
    private Boolean globalModelStatus;

    @ApiModelProperty(value = "增值业务Id")
    private String vasId;

    @ApiModelProperty(value = "增值业务code")
    private String vasCode;

    @ApiModelProperty(value = "公司Id")
    private String companyId;

    @ApiModelProperty(value = "平台公司Id")
    private String platformCompanyId;

    @ApiModelProperty(value = "计价方式")
    private Integer priceMethod;

    @ApiModelProperty(value = "单价")
    private BigDecimal perPrice;
}
