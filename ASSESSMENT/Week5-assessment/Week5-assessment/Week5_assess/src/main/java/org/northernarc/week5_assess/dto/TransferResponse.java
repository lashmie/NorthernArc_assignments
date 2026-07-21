package org.northernarc.week5_assess.dto;

import java.math.BigDecimal;

public class TransferResponse {
    private String referenceId;
    private BigDecimal amount;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
