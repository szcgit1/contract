package com.xtm.contract.model.vo.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author tong
 * @version 1.0
 * @date 2021/6/29 21:15
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplementContractInfo {
    /**
     * 补充合同ID
     */
    @ApiModelProperty(value = "补充合同ID")
    private String supplementContractId;
    /**
     * 补充合同编号
     */
    @ApiModelProperty(value = "补充合同编号")
    private String supplementContractCode;

    /**
     * 是否是主合同(0：主合同，1：子合同)
     */
    @ApiModelProperty(value = "是否是主合同(0：主合同，1：子合同)")
    private int mainFlag;
}
