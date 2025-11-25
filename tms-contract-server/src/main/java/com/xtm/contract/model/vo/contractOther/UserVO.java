package com.xtm.contract.model.vo.contractOther;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("用户信息")
public class UserVO {
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "用户姓名")
    private String name;
    @ApiModelProperty(value = "手机号")
    private String mobile;
    @ApiModelProperty(value = "固定电话")
    private String fixedTelephone;
    @ApiModelProperty(value = "联系地址")
    private String contactAddressId;
    @ApiModelProperty(value = "大头照")
    private String headPhotoId;
    @ApiModelProperty(value = "身份证正面照")
    private String idcardFrontPhotoId;
    @ApiModelProperty(value = "身份证反面照")
    private String idcardBackPhotoId;
    @ApiModelProperty(value = "禁用状态")
    private Integer enbaleStatus;
    @ApiModelProperty(value = "认证状态")
    private Integer certificationState;
    @ApiModelProperty(value = "激活状态")
    private Integer activatedState;
    @ApiModelProperty(value = "公司id")
    private String companyId;
    @ApiModelProperty(value = "功能权限类型")
    private Integer funcitonPermissionType;
    @ApiModelProperty(value = "数据权限类型")
    private Integer dataPermissionType;
    @ApiModelProperty(value = "控制状态")
    private Integer controlledState;
    @ApiModelProperty(value = "身份证号")
    private String idcardNo;
    @ApiModelProperty(value = "登陆用户名")
    private String loginName;
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "审批状态")
    private Integer approvalStatus;
    @ApiModelProperty(value = "驾驶证")
    private String driverLicencePhoneId;
    @ApiModelProperty(value = "驾龄")
    private Integer drivingYears;
    @ApiModelProperty(value = "描述")
    private String description;
    @ApiModelProperty(value = "是否司机")
    private Integer isDriver;
    @ApiModelProperty(value = "信用分数")
    private BigDecimal creditCount;
    @ApiModelProperty(value = "是否vip")
    private Integer isVip;
    @ApiModelProperty(value = "人脸识别照")
    private String qrcodePhotoId;
    @ApiModelProperty(value = "用户类型")
    private Integer userCategory;
    @ApiModelProperty(value = "行驶证")
    private String drivingLicensePhotoId;
    @ApiModelProperty(value = "用户照")
    private String recentPhotoId;
    @ApiModelProperty(value = "指纹")
    private String fingerprintsPhotoId;
    @ApiModelProperty(value = "第三方用户id")
    private String thirdUserId;
    @ApiModelProperty(value = "是否基站定位")
    private Integer lbsStatus;
    @ApiModelProperty(value = "公司合并备注")
    private String mergerComment;
    @ApiModelProperty(value = "身份证有效期开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date effectiveStartDate;
    @ApiModelProperty(value = "身份证有效期结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date effectiveEndDate;
    @ApiModelProperty(value = "身份证是否永久")
    private Integer isEndless;
    @ApiModelProperty(value = "性别")
    private String gender;
    @ApiModelProperty(value = "是否签订合同协议")
    private Integer isContractAgreement;
    @ApiModelProperty(value = "其他照片")
    private String otherPhotoId;
    @ApiModelProperty(value = "协议版本")
    private Integer agreementProtocolVer;
    @ApiModelProperty(value = "手持照片")
    private String holdingIdcardPhotoId;
    @ApiModelProperty(value = "人脸识别结果")
    private Integer faceRecognitionResult;
    @ApiModelProperty(value = "税务上报编码")
    private String taxUploadCode;
    @ApiModelProperty(value = "个人印章模板id")
    private String personalSealTemplateId;
    @ApiModelProperty(value = "是否删除")
    private Integer isDelete;
    @ApiModelProperty(value = "版本")
    private Integer ver;
    @ApiModelProperty(value = "创建人")
    private String creater;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "修改人")
    private String modifier;
    @ApiModelProperty(value = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date modifyTime;
}