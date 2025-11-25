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

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同扩展表")
@TableName(value = "contract_ext")
public class ContractExt {
    @ApiModelProperty("主鍵")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "合同id")
    @TableField("CONTRACT_ID")
    private String contractId;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty(value = "签署方式",notes = "0 是ecb  1是法大大，默认0")
    @TableField("SIGN_TYPE")
    private Integer signType;

    @ApiModelProperty(value = "身份证号")
    @TableField("CARD_NO")
    private String cardNo;

}
