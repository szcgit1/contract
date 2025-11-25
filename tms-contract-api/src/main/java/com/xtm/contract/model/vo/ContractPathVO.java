package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ContractPathVO {
	/**
	 * 电子合同URL
	 */
	@ApiModelProperty("电子合同URL")
	private String econtractUrl;
	/**
	 * 失败原因编码
	 */
	@ApiModelProperty("失败原因编码")
	private Integer resultCode;
	/**
	 * 失败原因描述
	 */
	@ApiModelProperty("失败原因描述")
	private String  resultDesc;
}
