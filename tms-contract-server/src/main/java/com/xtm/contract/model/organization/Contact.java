package com.xtm.contract.model.organization;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import java.io.Serializable;
import java.util.Date;

/**
 * 联系人
 */
@ApiModel(value="com.xiaoniu.tms.organization.model.domain.Contact",description="联系人")
@Data()
@TableName(value = "xn_m_contact")
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class Contact implements Serializable {
    @ApiModelProperty("ID")
    private String id;

    /**
     * 姓名
     */
    @ApiModelProperty("姓名")
    private String name;

    /**
     * 公司名称
     */
    @ApiModelProperty("公司名称")
    private String company;

    /**
     * 固定电话
     */
    @ApiModelProperty("固定电话")
    private String fixedTelephone;

    /**
     * 地址ID
     */
    @ApiModelProperty(value = "地址ID", hidden = true)
    private String addressId;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;

    /**
     * 传真
     */
    @ApiModelProperty("传真")
    private String fax;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人" , hidden = true)
    private String creater;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人", hidden = true)
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", hidden = true)
    private Date modifyTime;

    /**
     * 数据版本
     */
    @ApiModelProperty(value = "数据版本", hidden = true)
    private Integer ver;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String mail;

    /**
     * 身份证号码
     */
    @ApiModelProperty("身份证号码")
    private String idcardNo;

    @ApiModelProperty(value = "统一社会信用代码")
    private String unifiedSocialCreditIdentifier;
}