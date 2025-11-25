package com.xtm.contract.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * @author : lushuai
 * @date :  2021/6/6 11:41
 * @description :
 */
@Data
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@ApiModel("业务关联扩展字段保存")
@AllArgsConstructor
public class BusinessExpansionFieldReq {


    @ApiModelProperty("扩展字段ID")
    private String expansionFieldId;

    @ApiModelProperty(value = "扩展字段 Value")
    private String columnValue;

    @ApiModelProperty(value = "扩展字段业务类型  eg:订单，货源")
    private String documentType;

    @ApiModelProperty(value = "扩展字段业务id  eg:订单，货源")
    private String documentId;

    @ApiModelProperty(value = "是否关联调度单 0 否 1 是")
    private Integer otherDocumentDispatch;

    @ApiModelProperty(value = "扩展字段枚举值")
    private List<BusinessExpansionEnumFieldReq> businessExpansionEnumFields;

}
