package com.xtm.contract.model.vo.contractTemplate;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateCreUpdVO {
	@ApiModelProperty("模板id")
	private String templateID;//模板id
}
