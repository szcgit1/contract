package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "上传文件信息")
public class EUploadFileDTO {
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    @ApiModelProperty(value = "文件名称,带扩展名")
    private String fileName;
    /**文件流程id*/
    @ApiModelProperty(value = "文件流程id，下载文件必传")
    private String flowId;
    /**业务类型*/
    @ApiModelProperty(value = "业务类型", required = true)
    private String businessType;
}
