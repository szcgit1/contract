package com.xtm.contract.model.vo.contract;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: zt
 * @Desc:
 * @date: 2021/4/16 15:07
 * @version: 1.0
 */
@Data
public class EcContractVo {
    @ApiModelProperty(value = "合同id")
    private String contractID;
    /**合同编号*/
    @ApiModelProperty(value = "合同编号")
    private String contractCode;
    /**电子合同地址*/
    @ApiModelProperty(value = "合同地址")
    private String contractUrl;
    /**电子合同地址*/
    @ApiModelProperty(value = "电子合同地址")
    private String ecContractUrl;
    /**电子合同文件名，带扩展名*/
    @ApiModelProperty(value = "电子合同文件名，带扩展名")
    private String ecContractFileName;
}
