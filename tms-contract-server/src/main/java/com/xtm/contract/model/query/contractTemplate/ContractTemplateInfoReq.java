package com.xtm.contract.model.query.contractTemplate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("合同模板信息查询入参")
public class ContractTemplateInfoReq {
	/**
	 * 合同模板id
	 */
	@ApiModelProperty("合同模板ID")
	private String templateId;

	/**
	 * 合同模板名称
	 */
	@ApiModelProperty("合同模板名称")
	private String templateName;

	/**
	 * 合同模板类型
	 */
	@ApiModelProperty("合同模板类型")
	private String contractType;

	/**
	 * 合同模板单据类型
	 */
	@ApiModelProperty("合同模板单据类型")
	private Integer contractDocumentType;

	/**
	 * 启用状态
	 */
	@ApiModelProperty("启用状态")
	private Integer enabledStatus;

	/**
	 * 是否系统默认
	 */
	@ApiModelProperty("是否系统默认")
	private int isDefault;
}
