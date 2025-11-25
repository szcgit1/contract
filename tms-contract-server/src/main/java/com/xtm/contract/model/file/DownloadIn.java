package com.xtm.contract.model.file;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 文件下载入参
 */
@Data
public class DownloadIn {
    /**
     * 文件路径
     */
    @ApiModelProperty(value = "文件URL",required = true)
    public String filePath;
    /**
     * 保存路径
     */
    @ApiModelProperty(value = "文件保存路径",required = true)
    public String savePath;
    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称",required = true)
    public String fileName;
}
