package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class UpdateAccumulateOrdersMainQuantityParam {

    @ApiModelProperty(value = "销售合同id")
    private String salesContractId;

    private Integer systemSource;

    /**
     * 合同货物累计订单主数量集合
     */
    @ApiModelProperty(value = "合同货物累计订单主数量集合")
    private List<ContractGoodsOrdersMainQuantityParam> goodsQuantityList;
}
