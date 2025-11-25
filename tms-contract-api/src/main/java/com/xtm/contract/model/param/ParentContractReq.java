package com.xtm.contract.model.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @package: com.xiaoniu.contract.model.query.contract.ParentContractReq
 * @author: wwh
 * @create: 2025-03-07 10:15
 * @description: 批量联合运单内容
 **/
@Data
public class ParentContractReq implements Serializable {
    private static final long serialVersionUID = -6683412146514631802L;
    /**
     * 单据ID
     */
    @ApiModelProperty(value = "单据ID",required = true)
    private String documentId;

    /**
     * 单据号
     */
    @ApiModelProperty(value = "单据code",required = true)
    private String documentCode;

    @ApiModelProperty(value = "合同类型",required = true)
    private Integer contractType;

}
