package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ContractGoodsOrdersMainQuantityParam {

    @ApiModelProperty(value = "合同货物id")
    private String contractGoodsId;

    @ApiModelProperty(value = "合同货物累计订单主数量")
    private BigDecimal mainOrdersQuantity;
}
