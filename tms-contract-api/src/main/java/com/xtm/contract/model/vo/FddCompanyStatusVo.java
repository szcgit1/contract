package com.xtm.contract.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.vo.fdd.FddCompanyStatusVo
 * @author: wwh
 * @create: 2025-05-06 15:42
 * @description: 法大大公司认证信息对象
 **/
@Data
public class FddCompanyStatusVo implements Serializable {
    private static final long serialVersionUID = 395067200215303565L;
    @ApiModelProperty("公司id")
    private String companyId;
    @ApiModelProperty("公司名称")
    private String companyName;
    @ApiModelProperty("法大大客户编号")
    private String customerId;
    @ApiModelProperty("认证状态 已认证：1；未认证：0',")
    private Integer verifyStatus;
    @ApiModelProperty("授权自动签状态 已授权：1；未授权：0")
    private Integer authAutoSignStatus;
}
