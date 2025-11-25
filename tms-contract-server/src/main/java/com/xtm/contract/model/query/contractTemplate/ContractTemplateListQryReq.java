package com.xtm.contract.model.query.contractTemplate;


import com.xtm.contract.model.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("合同模板列表查询入参")
public class ContractTemplateListQryReq extends Page {

	@ApiModelProperty(value = "模板名称")
	private String contractTemplateName;

	@ApiModelProperty(value = "当前登陆人公司id")
	private String companyId;

	@ApiModelProperty(value = "合同模板类型",notes = "2961010:框架合同，2961010：明细合同")
	private Integer templateContractType;

	@ApiModelProperty(value = "启用状态",notes = "1:启用，0：禁用")
	private Integer enabledStatus;

	@ApiModelProperty(value = "合同模板单据类型",notes = "1133000:订单，1133030：运单")
	private Integer templateDocumentType;

	@ApiModelProperty(value = "适用对象名称")
	private String applicableObjectName;

	@ApiModelProperty(value = "是否系统默认")
	private int isDefault;

	@ApiModelProperty(value = "创建起始时间")
	private String createStartTime;

	@ApiModelProperty(value = "创建截止时间")
	private String createEndTime;

	@ApiModelProperty(value = "业务性质（5021010：双方合同；5021000：三方合同）")
	private Integer businessType;
}
