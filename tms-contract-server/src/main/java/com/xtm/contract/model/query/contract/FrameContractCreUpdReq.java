package com.xtm.contract.model.query.contract;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("框架合同创建修改入参")
public class FrameContractCreUpdReq {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("合同id")
	private String contractID;//合同id
	@ApiModelProperty(value = "合同类型",required = true)
	private Integer contractType;//合同类型
	@ApiModelProperty(value = "合同编号",required = true)
	private String contractCode;//合同编号
	@ApiModelProperty(value = "合同模版名称",required = true)
	private String templateName;//合同模版名称
	@ApiModelProperty(value = "合同标题",required = true)
	private String title;//合同标题
	@ApiModelProperty(value = "合同内容",required = true)
	private String content;//合同内容
	@ApiModelProperty(value = "委托人公司id",required = true)
	private String trustorCompanyId;//委托人公司id
	@ApiModelProperty(value = "承运人公司id",required = true)
	private String carrierCompanyId;//承运人公司id
	/*@ApiModelProperty(value = "委托人公司名称",required = true)
	private String trustorCompanyName;
	@ApiModelProperty(value = "承运人公司名称",required = true)
	private String carrierCompanyName;*/
	@ApiModelProperty(value = "起始有效期",required = true)
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	private Date startDate;//起始有效期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	@ApiModelProperty(value = "截止有效期",required = true)
	private Date endDate;//截止有效期
	@JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
	@ApiModelProperty(value = "签署日期",required = true)
	private Date contractDate;//签署日期
	@ApiModelProperty(value = "合同模板id")
	private String contractTemplateID;//合同模板id
	@ApiModelProperty("合同删除状态")
	private Integer isDelete;//合同删除状态
	@ApiModelProperty(value = "策略ID")
	private String strategyId;
	@ApiModelProperty(value = "策略名称")
	private String strategyName;
	@ApiModelProperty(value = "合同附件列表")
	private List<String> fileIds;

	@ApiModelProperty("合同方向:0甲方，1乙方,2丙方")
	private Integer direction;

	@ApiModelProperty(value = "代办人公司id")
	private String agentCompanyId;

	@ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
	private Integer contractTemplateBusinessType;

	@ApiModelProperty(value = "运输方式",notes = "1041000L：汽运；1041030L：海运")
	private Integer transportMode;
}
