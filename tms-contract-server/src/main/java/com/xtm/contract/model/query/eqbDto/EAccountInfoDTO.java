package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ApiModel(value = "账户信息")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EAccountInfoDTO {
    /**
     * 个人-用户唯一标识
     * 机构-机构唯一标识
     * */
    @ApiModelProperty(value = "(个人或机构)-用户唯一标识", required = true)
    private String thirdPartyUserId;
    /**
     * 机构必传
     * 创建人个人账号id（调用个人账号创建接口返回的accountId）
     * */
    @ApiModelProperty(value = "创建者（调用个人账号创建接口返回的accountId）")
    private String creator;
    /**
     * 个人-用户姓名
     * 机构-机构名称
     **/
    @ApiModelProperty(value = "用户姓名或机构名称", required = true)
    private String name;
    /**
     * 个人-身份证号
     * 机构-统一社会信用代码
     * */
    @ApiModelProperty(value = "个人-身份证号, 机构-统一社会信用代码", required = true)
    private String idNumber;
    /**手机号*/
    @ApiModelProperty(value = "手机号")
    private String mobile;
    /**邮箱*/
    @ApiModelProperty(value = "邮箱")
    private String email;
    /**企业法定代表人证件号*/
    @ApiModelProperty(value = "企业法定代表人证件号")
    private String orgLegalIdNumber;
    /**企业法定代表人名称*/
    @ApiModelProperty(value = "企业法定代表人名称")
    private String orgLegalName;

    /**业务类型*/
    @ApiModelProperty(value = "业务类型", required = true)
    private String businessType;

    /**查询账户类型*/
    @ApiModelProperty(value = "查询账户类型-0:个人账户 1:机构账户")
    private int type;

    /**账户id （企业实名认证接口必传）*/
    @ApiModelProperty(value = "账户id")
    private String accountId;
}