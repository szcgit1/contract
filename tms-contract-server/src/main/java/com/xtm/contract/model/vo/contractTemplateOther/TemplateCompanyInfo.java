package com.xtm.contract.model.vo.contractTemplateOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zhoutong
 * @version 1.0
 * @date 2021/11/30 11:21
 * @desc 适用公司信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateCompanyInfo {
    @ApiModelProperty("公司id")
    private String companyId;
    @ApiModelProperty("公司名")
    private String companyName;
}
