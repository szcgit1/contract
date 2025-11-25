package com.xtm.contract.model.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/26 16:15
 * @desc 合同货物表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同货物")
@TableName(value = "contract_goods")
public class ContractGoods {
    @ApiModelProperty("主鍵")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "合同ID")
    @TableField("CONTRACT_ID")
    private String contractId;

    @ApiModelProperty(value = "发货地址")
    @TableField("SEND_ADDRESS")
    private String sendAddress;

    @ApiModelProperty(value = "收货地址")
    @TableField("RECEIVE_ADDRESS")
    private String receiveAddress;

    @ApiModelProperty(value = "发货联系人姓名")
    @TableField("SEND_CONTACT_NAME")
    private String sendContactName;

    @ApiModelProperty(value = "收货联系人姓名")
    @TableField("RECEIVE_CONTACT_NAME")
    private String receiveContactName;

    @ApiModelProperty(value = "发货联系人手机号")
    @TableField("SEND_CONTACT_MOBILE")
    private String sendContactMobile;

    @ApiModelProperty(value = "收货联系人手机号")
    @TableField("RECEIVE_CONTACT_MOBILE")
    private String receiveContactMobile;

    @ApiModelProperty(value = "发货时间")
    @TableField("SEND_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date sendTime;

    @ApiModelProperty(value = "收货时间")
    @TableField("RECEIVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date receiveTime;

    @ApiModelProperty(value = "运输方式")
    @TableField("TRANSPORT_TYPE")
    private String transportType;

    @ApiModelProperty(value = "车辆编号")
    @TableField("VEHICLE_CODE")
    private String vehicleCode;

    @ApiModelProperty(value = "合同货物JSON")
    @TableField("CONTRACT_GOODS_JSON")
    private String contractGoodsJson;

    @ApiModelProperty(value = "是否删除")
    @TableField("IS_DELETE")
    private Integer isDelete;
}
