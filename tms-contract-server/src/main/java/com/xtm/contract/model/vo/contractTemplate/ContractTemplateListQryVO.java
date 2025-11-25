package com.xtm.contract.model.vo.contractTemplate;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplateListQryVO {
	@ApiModelProperty("合同模版列表")
	private IPage<ContractTemplateInfoQryVO> contractTemplateInfoQryVO;
}
