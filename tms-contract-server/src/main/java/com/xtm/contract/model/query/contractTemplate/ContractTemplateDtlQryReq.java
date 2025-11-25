package com.xtm.contract.model.query.contractTemplate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateDtlQryReq {

	/**
	 * 合同模板id
	 */
	private String templateID;

	/**
	 * 合同模板类型
	 */
	private Integer contractTemplateType;

	/**
	 * 合同模版单据类型
	 */
	private Integer contractDocumentType;

	/**
	 * 是否系统默认
	 */
	private int isDefault;
	
}
