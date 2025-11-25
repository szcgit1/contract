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

import java.io.Serializable;
import java.util.Date;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/3/14 18:46
 * @version: 1.0
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "合同模板")
@TableName(value = "contract_template")
public class ContractTemplate implements Serializable {
    @TableId("ID")
    private String id;

    @ApiModelProperty(value = "模版名称")
    @TableField("TEMPLATE_NAME")
    private String templateName;

    @ApiModelProperty(value = "主题")
    @TableField("TITLE")
    private String title;

    @ApiModelProperty(value = "是否默认")
    @TableField("IS_DEFAULT")
    private Boolean isDefault;

    @ApiModelProperty(value = "签章照片")
    @TableField("SIGN_PHOTO_ID")
    private String signPhotoId;

    @ApiModelProperty(value = "公司ID")
    @TableField("COMPANY_ID")
    private String companyId;

    @ApiModelProperty(value = "合同类型")
    @TableField("CONTRACT_TYPE")
    private Integer contractType;

    @ApiModelProperty(value = "合同单据类型")
    @TableField("CONTRACT_DOCUMENT_TYPE")
    private Integer contractDocumentType;

    @ApiModelProperty(value = "适用对象类型 ")
    @TableField("APPLICABLE_OBJECT_TYPE")
    private Integer applicableObjectType;

    @ApiModelProperty(value = "创建者")
    @TableField("CREATER")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("CREATE_TIME")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFIER")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @TableField("MODIFY_TIME")
    private Date modifyTime;

    @ApiModelProperty(value = "版本")
    @TableField("VER")
    private Integer ver;

    @ApiModelProperty(value = "模版内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "启用状态",notes = "1:启用，0：禁用")
    @TableField("ENABLED_STATUS")
    private Integer enabledStatus;

    @ApiModelProperty(value = "是否删除",notes = "0:未删除，1：已删除")
    @TableField("IS_DELETE")
    private Integer isDelete;

    @ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
    @TableField("BUSINESS_TYPE")
    private Integer businessType;
}