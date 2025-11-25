package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tong
 * @version 1.0
 * @date 2021/8/16 17:20
 * @desc
 */
@Data
public class ApplyCompanyInfo {
    @ApiModelProperty(value = "适用公司ID")
    private String companyId;

    @ApiModelProperty(value = "适用公司名称")
    private String companyName;
}
