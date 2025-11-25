package com.xtm.contract.model.dto.contract;

import com.xtm.contract.model.domain.SalesContract;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SalesContractHistorySaveDTO extends SalesContract {

    @ApiModelProperty(value = "停用标记 0: 启用 1: 禁用")
    private Boolean disabled;

    @ApiModelProperty(value = "合同货物")
    private List<SalesContractGoodsHistorySaveDTO> contractGoodsHistorySaveDTOList;

    @ApiModelProperty(value = "合同条款")
    private List<SalesContractTermsHistorySaveDTO> contractTermsHistorySaveDTOS;
}
