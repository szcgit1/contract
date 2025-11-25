package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("公司信息")
public class CompanyFddVO {

	@ApiModelProperty(value = "公司Id")
	private String companyId;
	
	@ApiModelProperty(value = "公司名称")
	private String companyName;

	@ApiModelProperty(value = "认证状态")
	private Integer certificationStatus;

	@ApiModelProperty(value = "授权状态")
	private Integer authorizationStatus;

	/**
	 * 身份证号吗
	 */
	private String idcardNo;

	/**
	 * 企业信用代码
	 */
	private String unifiedSocialCreditIdentifier;
}
