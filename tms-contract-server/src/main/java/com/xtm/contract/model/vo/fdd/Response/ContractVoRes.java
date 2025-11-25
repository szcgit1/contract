package com.xtm.contract.model.vo.fdd.Response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @package: com.xiaoniu.contract.model.vo.fdd.ContractVo
 * @author: wwh
 * @create: 2025-04-22 15:59
 * @description: 模板合同信息查询
 **/
@Data
@ApiModel("模板合同详情返回字段")
public class ContractVoRes implements Serializable {
    private static final long serialVersionUID = 4042592366883266170L;
    @ApiModelProperty("合同编码")
    private String contractCode;
    @ApiModelProperty("合同名称")
    private String title;
    @ApiModelProperty("托运人id")
    private String trustorCompanyId;
    @ApiModelProperty("托运人名称")
    private String trustorCompanyName;
    @ApiModelProperty("承运人id")
    private String carrierCompanyId;
    @ApiModelProperty("承运人名称")
    private String carrierCompanyName;
    @ApiModelProperty("托运代办人id")
    private String agentCompanyId;
    @ApiModelProperty("托运代办人名称")
    private String agentCompanyName;
    @ApiModelProperty("合同起始有效期")
    private LocalDate validStartDate;
    @ApiModelProperty("合同截至有效期")
    private LocalDate validEndDate;
    @ApiModelProperty("签署日期")
    private LocalDate contractDate;
    @ApiModelProperty("合同url")
    private String fileUrl;
    @ApiModelProperty(value = "合同Id",hidden = true)
    private String fileId;
}
