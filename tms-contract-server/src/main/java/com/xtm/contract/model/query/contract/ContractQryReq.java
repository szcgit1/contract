package com.xtm.contract.model.query.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractQryReq {
	@ApiModelProperty("合同ID集合")
	private List<String> ids;

	@ApiModelProperty("合同编号")
	private String contractCode;

	@ApiModelProperty("合同类型")
	private Integer contractType;

	@ApiModelProperty("单据号集合")
	private List<String> documentIds;

	@ApiModelProperty("合同模版id集合")
	private List<String> contractTemplateIds;

	@ApiModelProperty("是否生效")
	private String validFlag;
}
