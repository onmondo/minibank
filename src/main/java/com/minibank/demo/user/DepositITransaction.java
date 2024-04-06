package com.minibank.demo.user;

import java.time.LocalDateTime;

public class DepositITransaction implements ITransaction {
    private Long id;
    private LocalDateTime timestamp;
    private BankAccount bankAccount;
    private Double amount;
    private String type;

    public DepositITransaction(String type) {
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void process() throws Exception {
        Double currentBalance = this.bankAccount.getBalance();
        Double newBalance = currentBalance + this.amount;
        if (newBalance <= 0) {
            throw new Exception("Insufficient balance to withdraw");
        }

        this.bankAccount.setBalance(newBalance);
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setAmount(Double amount) throws Exception {
        if (amount <= 0) {
            throw new Exception("Invalid amount");
        }
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getType() {
        return type;
    }

    public Double getAmount() {
        return amount;
    }
}