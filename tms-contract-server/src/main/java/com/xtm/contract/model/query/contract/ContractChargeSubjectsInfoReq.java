package com.xtm.contract.model.query.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractChargeSubjectsInfoReq {

    @ApiModelProperty("科目名称")
    private String feesDesc;


    @ApiModelProperty("单价")
    private BigDecimal price;


    @ApiModelProperty("订单：合同或结算/运单：小计")
    private BigDecimal subtotal;
}
