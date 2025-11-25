package com.xtm.contract.model.vo;

import com.xtm.contract.model.vo.FileInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 框架合同附件查询返回
 */
@Data
public class ContractFrameRsp {

    @ApiModelProperty(value = "合同id")
    private String contractId;

    @ApiModelProperty(value = "合同编号")
    private String contractCode;

    @ApiModelProperty(value = "合同地址")
    private String contractUrl;

    @ApiModelProperty("合同方向:0甲方，1乙方,2丙方")
    private Integer direction;

    @ApiModelProperty(value = "业务性质",notes = "5021010：双方合同；5021000：三方合同")
    private Integer businessType;


    @ApiModelProperty("合同附件列表")
    private List<FileInfo> fileInfos;

}
