package com.xtm.contract.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @author : lushuai
 * @date :  2021/6/6 11:42
 * @description :
 */
@Data
@NoArgsConstructor
@SuperBuilder
@Accessors(chain = true)
@ApiModel("业务关联扩展字段保存")
@AllArgsConstructor
public class BusinessExpansionEnumFieldReq {


    @ApiModelProperty(value = "枚举Id")
    private String enumColumnId;

    @ApiModelProperty(value = "枚举值")
    private String enumColumnValue;
}
