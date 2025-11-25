package com.xtm.contract.model.vo.fdd;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @package: com.xiaoniu.contract.model.vo.fdd.ContractReq
 * @author: wwh
 * @create: 2025-04-22 16:44
 * @description: 框架合同查询
 **/
@Data
@ApiModel("框架合同查询")
public class ContractReq implements Serializable {
    private static final long serialVersionUID = -3966454645680964284L;
    @ApiModelProperty("公司名称")
    private String companyId;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    @ApiModelProperty("授权公司id")
    private List<String> authCompanyIds;
}
