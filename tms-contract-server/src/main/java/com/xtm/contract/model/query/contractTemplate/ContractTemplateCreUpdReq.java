package com.xtm.contract.model.query.contractTemplate;

import com.xtm.contract.model.query.contractOther.ApplyCompanyInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("合同模板新增修改入参")
public class ContractTemplateCreUpdReq {
	@ApiModelProperty(value = "模板id")
	private String templateID;

	@ApiModelProperty(value = "模板名称")
	@NotBlank(message = "模板名称不可为空")
	private String templateName;

	@ApiModelProperty(value = "模板合同类型")
	@NotNull(message = "模板合同类型不可为空")
	private Integer contractType;

	@ApiModelProperty(value = "合同模版单据类型")
    private Integer contractDocumentType;

	@ApiModelProperty(value = "适用对象类型")
	private Integer applicableObjectType;

	@ApiModelProperty(value = "标题")
	@NotBlank(message = "标题不可为空")
	private String title;

	@ApiModelProperty(value = "内容")
	@NotBlank(message = "内容不可为空")
	private String content;

	@ApiModelProperty(value = "签章图片id")
	private String signPhotoId;

	@ApiModelProperty(value = "适用企业ID")
	private List<ApplyCompanyInfo> applyCompanyInfos;

	@ApiModelProperty(value = "适用会员类型")
	private List<Integer> memberTypes;

	@ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
	private Integer businessType;
}
