package com.xtm.contract.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsQuantityDTO {

    private Long contractId;

    /**
     * 累计订单主数量
     */
    private BigDecimal mainOrdersQuantity;
}
