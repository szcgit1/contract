package com.xtm.contract.model.vo.contractTemplate;

import com.xtm.contract.model.query.contractOther.ApplyCompanyInfo;
import com.xtm.contract.model.vo.FileInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateDtlQryVO {
	@ApiModelProperty("合同模板id")
	private String templateID;
	@ApiModelProperty("合同模板名称")
	private String templateName;
	@ApiModelProperty("合同模板类型")
	private Integer contractType;
	@ApiModelProperty("合同模板单据类型")
	private Integer contractDocumentType;
	@ApiModelProperty("标题")
	private String title;
	@ApiModelProperty("正文")
	private String content;
	@ApiModelProperty("是否系统默认：0 默认，1 自定义")
	private int isDefault;
	@ApiModelProperty("机构类型")
	private Integer orgType;
	@ApiModelProperty(value = "签署照片")
	private FileInfo signPhotoInfo;

	@ApiModelProperty(value = "适用公司集合")
	List<ApplyCompanyInfo> applyCompanyList;

	@ApiModelProperty(value = "适用会员集合")
	List<Long> applyMemberList;

	@ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
	private Integer businessType;
}
