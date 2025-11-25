package com.xtm.contract.model.param;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 框架合同附件查询参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractFrameReq {


	@ApiModelProperty(value = "委托人公司id",required = true)
	private String trustorCompanyId;

	@ApiModelProperty(value = "承运人公司id",required = true)
	private String carrierCompanyId;

	@ApiModelProperty(value = "代办人公司id")
	private String agentCompanyId;

	@ApiModelProperty("合同方向:0甲方，1乙方,2丙方")
	private Integer direction;

	@ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
	private Integer contractTemplateBusinessType;

	@ApiModelProperty(value = "调度时间")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", locale = "zh", timezone = "GMT+8")
	private Date dispatchTime;;

	@ApiModelProperty(value = "运输方式",notes = "1041000L：汽运；1041030L：海运")
	private Long transportMode;



}
