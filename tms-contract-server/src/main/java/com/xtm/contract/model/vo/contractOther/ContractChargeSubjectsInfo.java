package com.xtm.contract.model.vo.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/28 17:21
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractChargeSubjectsInfo {
    @ApiModelProperty("科目名称")
    private String feesDesc;

    @ApiModelProperty("合同单价")
    private BigDecimal contractUnitPrice;

    @ApiModelProperty("结算单价")
    private BigDecimal settleUnitPrice;

    @ApiModelProperty("合同金额")
    private BigDecimal contractPrice;

    @ApiModelProperty("结算金额")
    private BigDecimal settlePrice;

    @ApiModelProperty("版本")
    private Integer ver;
}
