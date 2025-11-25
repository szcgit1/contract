package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/14 22:20
 * @desc
 */
@Data
public class PartnerFraContractVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private String contractId;
    @ApiModelProperty("合同编号")
    private String contractCode;
    @ApiModelProperty("托运方公司ID")
    private String trustorCompanyId;
    @ApiModelProperty("承运方公司ID")
    private String carrierCompanyId;
}
