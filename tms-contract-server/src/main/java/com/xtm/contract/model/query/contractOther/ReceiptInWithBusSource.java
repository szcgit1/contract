package com.xtm.contract.model.query.contractOther;

import com.xtm.contract.enums.FileBizSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 电子回单/签章
 */
@Data
public class ReceiptInWithBusSource extends BaseIn{
    @ApiModelProperty(value = "文件Base64" , required = true)
    public String base64;
    @ApiModelProperty(value = "文件名称" , required = true)
    public String fileName;

    public String bizTableName;
    public FileBizSourceEnum bizSource;
    public String operatorUserId;
}
