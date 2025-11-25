package com.xtm.contract.model.query.eqbDto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel(value = "签署文档")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESignDocumentDTO {
    /**上传文档待签署文档时返回的fileId*/
    private String fileId;
    /**文件名称，带扩展名，例如:xxx.pdf*/
    private String fileName;
    /**合同标题*/
    private String businessScene;
    /**签约账户*/
    private List<ESignAccountDTO> signAccounts;

    /**业务类型*/
    @ApiModelProperty(value = "业务类型")
    private String businessType;
}
