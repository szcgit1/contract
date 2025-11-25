package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/22 19:38
 * @desc
 */
@Data
public class ContractCodeQryVO {
    @ApiModelProperty(value = "合同ID")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同标题")
    private String title;

    @ApiModelProperty(value = "合同类型")
    private Integer contractType;
}
