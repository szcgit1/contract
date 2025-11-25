package com.xtm.contract.model.vo.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.vo.contract.DispatchGoodsInfoVo
 * @author: wwh
 * @create: 2025-03-06 11:47
 * @description: 调度单货物信息
 **/
@Data
public class DispatchGoodsInfoVo implements Serializable {
    private static final long serialVersionUID = -1250626680552055100L;
    @ApiModelProperty(value = "发货时间")
    private String sendTime;
    @ApiModelProperty(value = "收货时间")
    private String receiveTime;
    @ApiModelProperty(value = "发货地址")
    private String sendAddress;
    @ApiModelProperty(value = "收货地址")
    private String receiveAddress;
    @ApiModelProperty(value = "货物载重")
    private String goodMeasuring;
    @ApiModelProperty(value = "货物名称")
    private String goodsName;
    @ApiModelProperty(value = "货物描述")
    private String goodsDesc;
}
