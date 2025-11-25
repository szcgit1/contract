package com.xtm.contract.model.mq;

import lombok.Data;

@Data
public class FinanceFddCostContract {
    private String contractId;
    private boolean success;
    private Long callTIme;
    private String busId;

    private String topic;
}
