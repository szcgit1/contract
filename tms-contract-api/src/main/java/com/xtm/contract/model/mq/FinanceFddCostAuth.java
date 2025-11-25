package com.xtm.contract.model.mq;

import lombok.Data;

@Data
public class FinanceFddCostAuth {
    private String customerId;
    private boolean success;
    private Long callTIme;
    private String busId;

    private String topic;
}
