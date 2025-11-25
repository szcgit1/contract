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

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

/**
 * 三方销售合同货物数量DTO
 *
 * @author Tenshin
 * @version 0.0.1
 * @since 2025-09-15 10:52
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ThirdPartySalesContractGoodsQuantityDto", description = "三方销售合同货物数量DTO")
public class ThirdPartySalesContractGoodsQuantityDto extends BaseDto {
    
    /**
     * 销售合同ID
     */
    @NotBlank(message = "销售合同ID不能为空")
    @Size(max = 20, message = "销售合同ID长度不能超过20")
    @ApiModelProperty(value = "销售合同ID", required = true, dataType = "String", position = 1)
    private String salesContractId;
    
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
    @ApiModelProperty(value = "系统来源", required = true, dataType = "Integer", example = "1", position = 2)
    private TmsContractConstant.ThirdPartySystemSource systemSource;
    
    /**
     * 货物数量列表
     */
    @Valid
    @NotEmpty(message = "货物数量列表不能为空")
    @ApiModelProperty(value = "货物数量列表", required = true, position = 3)
    public List<GoodsQuantity> goodsQuantities;
    
    
    /**
     * 货物数量明细
     */
    @Getter
    @Setter
    @SuperBuilder
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @ApiModel(value = "GoodsQuantity", description = "货物数量明细")
    public static class GoodsQuantity extends BaseDto {
        
        /**
         * 合同货物ID
         */
        @NotBlank(message = "合同货物ID不能为空")
        @Size(max = 32, message = "合同货物ID长度不能超过32")
        @ApiModelProperty(value = "合同货物ID", required = true, dataType = "String", position = 1)
        private String contractGoodsId;
        
        /**
         * 累计订单主数量
         */
        @NotNull(message = "累计订单主数量不能为空")
        @PositiveOrZero(message = "累计订单主数量必须为正数或者0")
        @ApiModelProperty(value = "累计订单主数量", required = true, dataType = "BigDecimal", position = 2)
        private BigDecimal mainOrdersQuantity;
        
    }
    
}
