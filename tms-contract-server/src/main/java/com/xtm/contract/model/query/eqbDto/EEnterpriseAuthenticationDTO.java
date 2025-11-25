package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/26 15:56
 * @desc
 */
@Data
public class EEnterpriseAuthenticationDTO {
    @ApiModelProperty(value = "业务类型")
    private String businessType;
    @ApiModelProperty(value = "账户信息")
    private EAccountInfoDTO accountInfo;
}
