package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class NcSalesContractChangeParam {

    @ApiModelProperty(value = "变更人")
    private String changePerson;

    @ApiModelProperty(value = "变更日期")
    private LocalDateTime changeTime;

    @ApiModelProperty(value = "变更原因")
    private String changeReason;

    @ApiModelProperty(value = "备注")
    private String changeRemark;

    @ApiModelProperty(value = "nc版本号")
    private Integer version;
}
