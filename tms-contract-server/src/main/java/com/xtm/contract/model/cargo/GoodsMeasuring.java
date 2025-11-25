package com.xtm.contract.model.cargo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 货物计量
 */
@ApiModel(value="com.xtm.contract.model.cargo.GoodsMeasuring",description="货物计量")
@Data()
@TableName(value = "xn_m_goods_measuring")
@Builder
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class GoodsMeasuring {

    @ApiModelProperty(value="ID",name="id")
    private String id;

    @ApiModelProperty(value="数量",name="quantity")
    private Integer quantity;

    @ApiModelProperty(value="重量",name="weight")
    private BigDecimal weight;

    @ApiModelProperty(value="体积",name="volume")
    private BigDecimal volume;

    @ApiModelProperty(value="重量计量单位",name="weightUnit")
    private Integer weightUnit;

    @ApiModelProperty(value="数量计量单位",name="quantityUnit")
    private Integer quantityUnit;

    @ApiModelProperty(value="体积计量单位",name="volumeUnit")
    private Integer volumeUnit;

    @ApiModelProperty(value="创建者",name="creater", hidden = true)
    private String creater;

    @ApiModelProperty(value="创建时间",name="createTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value="修改人",name="modifier", hidden = true)
    private String modifier;

    @ApiModelProperty(value="修改时间",name="modifyTime", hidden = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    @ApiModelProperty(value="数据版本",name="ver", hidden = true)
    private Integer ver;

    @ApiModelProperty(value="货源计量id(订单作废还原余量使用到)")
    private String originId;
}