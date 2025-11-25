package com.xtm.contract.model.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : lushuai
 * @date :  2021/3/23 18:10
 * @description : 分页组件
 */
@Data
@ApiModel("通用分页组件")
public class Page {

    /**
     * 当前页
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("页码")
    private Integer pageNum ;

    /**
     * 每页数据量
     */
    @NotNull
    @Min(1)
    @ApiModelProperty("每页数据量")
    private Integer pageSize;
}
