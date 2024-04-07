package com.minibank.demo.user;

import com.minibank.demo.bank.BankAccount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReverseDepositITransaction implements ITransaction {
    private Long id;
    private LocalDateTime timestamp;
    private BankAccount bankAccount;
    private BigDecimal amount;
    private String type;

    public ReverseDepositITransaction(String type) {
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void process() throws Exception {
        BigDecimal currentBalance = this.bankAccount.getBalance();
        BigDecimal newBalance = currentBalance.add(this.amount);
        if (newBalance.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Reversal not allowed due to insufficient funds");
        }

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

    public LocalDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
