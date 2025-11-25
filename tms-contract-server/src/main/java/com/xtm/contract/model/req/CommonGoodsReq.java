package com.xtm.contract.model.req;

import com.xtm.contract.model.cargo.GoodsMeasuring;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : lushuai
 * @date :  2021/6/5 20:48
 * @description :
 */
@Data
@Accessors(chain = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("普通货物保存入参")
public class CommonGoodsReq {

    @ApiModelProperty(value = "货物Id")
    private String goodsId;

    @ApiModelProperty(value = "货物规格描述")
    private String model;

    @ApiModelProperty(value = "货物名称")
    private String goodsName;

    @ApiModelProperty(value = "货物编号")
    private String goodsCode;

    @ApiModelProperty(value = "货物类型")
    private Integer goodsType;

    @ApiModelProperty(value = "货物单价")
    private BigDecimal unitPrice;

    @ApiModelProperty(value = "货物计量")
    private GoodsMeasuring measuring;

    //集装箱相关信息
    @ApiModelProperty(value = "尺寸")
    private Integer size;

    @ApiModelProperty(value = "箱规")
    private String containerSize;

    @ApiModelProperty(value = "箱号")
    private String containerNo;

    @ApiModelProperty(value = "铅封号")
    private String sealNumber;

    @ApiModelProperty(value = "箱重")
    private BigDecimal containerWeight;

    @ApiModelProperty(value = "箱主")
    private String containerOwner;

    @ApiModelProperty(value = "扩展字段")
    private List<BusinessExpansionFieldReq> businessExpansionFields;

    @ApiModelProperty(value = "货物钢卷号")
    private String goodsSteelCodes;

    @ApiModelProperty(value = "常用货物表的id")
    private String goodsManageId;

    @ApiModelProperty(value = "nc货物明细id")
    private String ncSendOrderDetailId;
}
