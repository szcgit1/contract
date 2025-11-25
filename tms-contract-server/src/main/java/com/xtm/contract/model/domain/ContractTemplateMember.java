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
 * @date 2021/7/23 16:02
 * @desc 合同模板适用会员
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "模板适用会员")
@TableName("contract_template_member")
public class ContractTemplateMember {
    @ApiModelProperty("主鍵")
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "合同模板ID")
    @TableField("CONTRACT_TEMPLATE_ID")
    private String contractTemplateId;

    @ApiModelProperty(value = "会员类型")
    @TableField("COMPANY_MEMBER_TYPE")
    private Integer companyMemberType;

    @ApiModelProperty(value = "创建人")
    @TableField("CREATER")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFIER")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty(value = "版本")
    @TableField("VER")
    private Integer ver;
}
