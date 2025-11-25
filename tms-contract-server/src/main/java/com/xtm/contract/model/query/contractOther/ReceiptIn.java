package com.xtm.contract.model.query.contractOther;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 电子回单/签章
 */
@Data
public class ReceiptIn extends BaseIn{
    @ApiModelProperty(value = "文件Base64" , required = true)
    public String base64;
    @ApiModelProperty(value = "文件名称" , required = true)
    public String fileName;
}
