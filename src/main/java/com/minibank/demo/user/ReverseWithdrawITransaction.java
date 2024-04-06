package com.minibank.demo.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReverseWithdrawITransaction implements ITransaction {
    private Long id;
    private LocalDateTime timestamp;
    private BankAccount bankAccount;
    private BigDecimal amount;
    private String type;

    public ReverseWithdrawITransaction(String type) {
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getType() {
        return this.type;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void process() {
        BigDecimal currentBalance = this.bankAccount.getBalance();
        BigDecimal newBalance = currentBalance.add(this.amount);

        this.bankAccount.setBalance(newBalance);
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setAmount(BigDecimal amount) throws Exception {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Invalid amount");
        }
        this.amount = amount;
    }
}
