package com.minibank.demo.user;

import java.time.LocalDateTime;

public interface ITransaction {
    void process() throws Exception;
    void setBankAccount(BankAccount bankAccount);
    void setAmount(Double amount) throws Exception;
    void setId(Long id);
    LocalDateTime getTimestamp();
    String getType();
    Double getAmount();
}
