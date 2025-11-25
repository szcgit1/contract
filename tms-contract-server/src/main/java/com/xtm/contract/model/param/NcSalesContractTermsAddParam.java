package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NcSalesContractTermsAddParam {

    /**
     * 条款编号
     */
    @ApiModelProperty(value = "条款编码",required = false)
    private String termCode;

    /**
     * 条款名称
     */
    @ApiModelProperty(value = "条款名称",required = false)
    private String termName;

    /**
     * 条款内容
     */
    @ApiModelProperty(value = "条款内容",required = false)
    private String termContent;

}
