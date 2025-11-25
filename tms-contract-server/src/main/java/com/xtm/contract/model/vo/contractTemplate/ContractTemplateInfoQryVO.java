package com.xtm.contract.model.vo.contractTemplate;

import com.xtm.contract.model.domain.ContractTemplate;
import com.xtm.contract.model.domain.ContractTemplateCompany;
import com.xtm.user.model.vo.UserInfoVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/23 18:08
 * @desc
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("合同模板列表查询出参")
public class ContractTemplateInfoQryVO extends ContractTemplate {
    @ApiModelProperty("创建人")
    private UserInfoVo createrUserInfo;

    @ApiModelProperty("适用范围")
    private String applyCompanyName;

    @ApiModelProperty("合同类型描述")
    private String contractTypeDesc;

    @ApiModelProperty("单据类型描述")
    private String documentTypeDesc;

    @ApiModelProperty(value = "业务性质描述（5021010-双方合同；5021000-三方合同）")
    private String businessTypeDesc;

    @ApiModelProperty("是否在用")
    private boolean userFlag;

    @ApiModelProperty("特殊适用公司")
    private List<ContractTemplateCompany> applyCompanys;
}
