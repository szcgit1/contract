package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/12 21:14
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("伙伴框架合同查询入参")
public class FrameContractPartnerReq {
    @ApiModelProperty("伙伴公司ID集合")
    private List<String> memberCompanyIds;

    @ApiModelProperty("当前公司ID")
    private String companyId;

    @ApiModelProperty("伙伴类型")
    private Integer memberType;
}
