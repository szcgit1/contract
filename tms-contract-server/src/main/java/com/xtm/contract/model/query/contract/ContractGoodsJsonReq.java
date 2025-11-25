package com.xtm.contract.model.query.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/26 20:53
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractGoodsJsonReq {
    @ApiModelProperty("货物Id")
    private String goodsId;
    @ApiModelProperty("货物规格")
    private String model;
    @ApiModelProperty("货物名称")
    private String goodsName;
    @ApiModelProperty("货物编号")
    private String goodsCode;
    @ApiModelProperty("货物描述")
    private String goodsDesc;
    @ApiModelProperty("货物类型")
    private Integer goodsType;
    @ApiModelProperty("货物计量")
    private String goodMeasuring;
}
