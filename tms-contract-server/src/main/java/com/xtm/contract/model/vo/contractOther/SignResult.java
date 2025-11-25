package com.xtm.contract.model.vo.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SignResult {
	@ApiModelProperty("请求结果返回码")
	private String code;
	@ApiModelProperty("请求结果返回描述")
	private String msg;
	@ApiModelProperty("合同的PDF地址")
	private String pdfUrl;

	public SignResult(String code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}

	public SignResult() {
		super();
		//  Auto-generated constructor stub
	}
}
