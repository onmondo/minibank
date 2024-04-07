package com.minibank.demo.user;

import java.math.BigDecimal;

public class TransactionRequest {
    private BigDecimal amount;
    private String currency;
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }
    public void setCurrency() { this.currency = currency; }
}
