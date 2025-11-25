package com.xtm.contract.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 法大大电子签章
 */
@Data
public class FddElectricSealResp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名称
     */
    @ApiModelProperty(value = "用户名称")
    private String userName;

    /**
     * 平台客户唯一标识
     */
    @ApiModelProperty(value = "平台客户唯一标识")
    private String openId;

    /**
     * 客户编号
     */
    @ApiModelProperty(value = "法大大系统的客户编号")
    private String customerId;

    /**
     * 账号类型   1个人 2企业
     */
    @ApiModelProperty(value = "账号类型   1个人 2企业")
    private Integer accountType;

    /**
     * 实名认证交易号
     */
    @ApiModelProperty(value = "实名认证交易号")
    private String verifyTransNo;

    /**
     * 认证状态 已认证：1；未认证：0
     */
    @ApiModelProperty(value = "认证状态 已认证：1；未认证：0")
    private Integer verifyStatus;

    /**
     * 授权自动签状态 已授权：1；未授权：0
     */
    @ApiModelProperty(value = "授权自动签状态 已授权：1；未授权：0")
    private Integer authAutoSignStatus;

    /**
     * 授权自动签合同id
     */
    @ApiModelProperty(value = "授权自动签合同id")
    private String authAutoSignContractId;

    /**
     * 授权自动签交易Id
     */
    @ApiModelProperty(value = "授权自动签交易Id")
    private String authAutoSignTransId;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 认证url
     */
    @ApiModelProperty(value = "认证url")
    private String verifyUrl;

    /**
     * 授权自动签url
     */
    @ApiModelProperty(value = "授权自动签url")
    private String authAutoSignUrl;

    /**
     * 认证方式 0:人脸识别认证 1:三要素认证
     */
    @ApiModelProperty(value = "认证方式 0:人脸识别认证 1:三要素认证")
    private Integer verifyType;

    /**
     * 个人身份证号
     */
    @ApiModelProperty(value = "个人身份证号")
    private String idcardNo;
}