package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author ShunY
 * @Date 2021/12/29 16:43
 * @Version 1.0
 */
@ApiModel(value = "框架合同信息返回")
@Data
public class FrameContractVO {

    @ApiModelProperty("主鍵")
    private String id;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "电子合同URL（未盖章）")
    private String ecContractPathUrl;

    @ApiModelProperty(value = "电子合同URL（盖章）")
    private String ecContractPdfUrl;
}
