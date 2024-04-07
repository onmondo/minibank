package com.minibank.demo.user;

import com.minibank.demo.bank.BankAccount;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ITransaction {
    void process() throws Exception;
    void setBankAccount(BankAccount bankAccount);
    void setAmount(BigDecimal amount) throws Exception;
    void setId(Long id);
    LocalDateTime getTimestamp();
    String getType();
    BigDecimal getAmount();
}
