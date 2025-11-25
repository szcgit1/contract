package com.xtm.contract.model.query.listener;

import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * @author tong
 * @version 1.0
 * @date 2021/7/31 12:45
 * @desc
 */
public class EqbConsumeAccountReq extends ApplicationEvent {
    public EqbConsumeAccountReq(Object source) {
        super(source);
    }

    /*public EqbConsumeAccountReq(Object source, String consumeAccountId, String vasId, BigDecimal amount) {
        super(source);
        this.consumeAccountId = consumeAccountId;
        this.vasId = vasId;
        this.amount = amount;
    }*/

    public EqbConsumeAccountReq(Object source,String contractId) {
        super(source);
        this.contractId = contractId;
    }

    private String consumeAccountId;

    private String vasId;

    private BigDecimal amount;

    private Integer vasCode;

    private String contractId;

    public String getConsumeAccountId() {
        return consumeAccountId;
    }

    public void setConsumeAccountId(String consumeAccountId) {
        this.consumeAccountId = consumeAccountId;
    }

    public String getVasId() {
        return vasId;
    }

    public void setVasId(String vasId) {
        this.vasId = vasId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getVasCode() {
        return vasCode;
    }

    public void setVasCode(Integer vasCode) {
        this.vasCode = vasCode;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
