package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/24 16:39
 * @desc
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentInfoQryIn {
    @ApiModelProperty(value = "单据Id")
    private String documentId;

    @ApiModelProperty(value = "单据类型")
    private Integer documentType;

    @ApiModelProperty(value = "费用ID")
    private String chargeSubjectId;

    @ApiModelProperty(value = "付款进度版本")
    private String ver;
}
