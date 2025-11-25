package com.xtm.contract.model.query.contract;

import com.xtm.contract.model.common.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("合同列表查询入参")
public class ContractListQryReq extends Page {

	/**
	 * 合同编号
	 */
	@ApiModelProperty("合同编号")
	private String contractCode;

	/**
	 * 托运人公司名
	 */
	@ApiModelProperty("托运人公司名")
	private String trustorCompanyName;

	/**
	 * 承运人公司名
	 */
	@ApiModelProperty("承运人公司名")
	private String carrierCompanyName;
	/**
	 * 代办公司名
	 */
	@ApiModelProperty("代办公司名")
	private String agentCompanyName;

	/**
	 * 交易起始时间
	 */
	@ApiModelProperty("交易起始时间")
	private String tradeStartTime;
	/**
	 * 交易截止时间
	 */
	@ApiModelProperty("交易截止时间")
	private String tradeEndTime;

	/**
	 * 起始有效期
	 */
	@ApiModelProperty("起始有效期")
	private String startDate;

	/**
	 * 截止有效期
	 */
	@ApiModelProperty("截止有效期")
	private String endDate;

	/**
	 * 合同类型
	 */
	@ApiModelProperty("合同类型")
	private Integer contractType;

	/**
	 * 当前登陆用户所属的公司类型
	 */
//	@ApiModelProperty("当前登陆用户所属的公司类型")
//	private Long companyType;

	/**
	 * 当前登陆用户所属的公司Id
	 */
	@ApiModelProperty("当前登陆用户所属的公司Id")
	private String companyId;

	@ApiModelProperty("当前登陆用户Id")
	private String userId;
	/**
	 * 单据类型
	 */
	@ApiModelProperty("单据类型")
	private Integer documentCode;

	/**
	 * 单据类型
	 */
	@ApiModelProperty("单据类型")
	private Integer documentType;

	/**
	 * 托运方签署状态
	 */
	@ApiModelProperty("托运方签署状态")
	private Integer trustorSignStatus;

	/**
	 * 承运方签署
	 */
	@ApiModelProperty("承运方签署")
	private Integer carrierSignStatus;
	/**
	 * 代办方签署
	 */
	@ApiModelProperty("代办方签署")
	private Integer agentSignStatus;

	/**
	 * 子公司id
	 */
	@ApiModelProperty("子公司id")
	private String subCompanyId;

	/**
	 * 业务性质（5021010-双方合同；5021000-三方合同）
	 */
	@ApiModelProperty("业务性质（5021010-双方合同；5021000-三方合同）")
	private Integer businessType;

	@ApiModelProperty("是否顶级公司 是：true 否：false")
	private Boolean isTopLevelCompany;

	@ApiModelProperty("业务单据号")
	private String dispatchBatchCode;

	//=======================================下面为后端添加的属性，前端不使用===============
	/**
	 * 1:查询顶级合同 包括运单合同和联合运单合同 2查询联合运单子合同
	 */
	private Integer queryTop=1;

	/**
	 * 查询合同id,可以用于查询子运单合同
	 */
	private List<String> parentContractIds;

	private LocalDateTime createTimeStart;

	private LocalDateTime createTimeEnd;

	/**
	 * dispatchBatchCode字段搜索出来的父级 联合运单下合同id
	 */
	private String contractId;

	/**
	 * 合同类型
	 */
	@ApiModelProperty("合同类型")
	private List<Integer> contractTypeList;
}
