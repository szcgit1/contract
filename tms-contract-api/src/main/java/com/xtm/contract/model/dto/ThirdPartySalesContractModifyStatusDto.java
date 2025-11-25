package com.xtm.contract.model.dto;

import com.xtm.contract.constant.TmsContractConstant;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 三方销售合同修改状态DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-01 16:19
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ThirdPartySalesContractModifyStatusDto", description = "三方销售合同修改状态DTO")
public class ThirdPartySalesContractModifyStatusDto extends BaseDto {
    
    /**
     * 销售合同ID列表
     */
    @NotEmpty(message = "销售合同ID列表不能为空")
    @ApiModelProperty(value = "销售合同ID列表", required = true, dataType = "List", example = "[\"114514\", \"1919\"]", position = 1)
    private List<@NotBlank(message = "主键ID元素不能为空") String> salesContractIds;
    
    /**
     * 合同状态
     * <br>
     * 0: 启用
     * <br>
     * 1: 禁用
     */
    @NotNull(message = "合同状态不能为空")
    @ApiModelProperty(value = "合同状态", required = true, dataType = "Integer", example = "1", position = 2)
    private Integer status;
    
    /**
     * 系统来源
     * <br>
     * 0: TMS
     * <br>
     * 1: NC
     * <br>
     * 2: 新网
     * <br>
     *
     * @see TmsContractConstant.ThirdPartySystemSource
     */
    @NotNull(message = "系统来源不能为空")
    @ApiModelProperty(value = "系统来源", required = true, dataType = "Integer", example = "1", position = 3)
    private TmsContractConstant.ThirdPartySystemSource systemSource;
    
}
