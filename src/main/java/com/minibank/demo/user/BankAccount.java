package com.minibank.demo.user;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class BankAccount {
    @Id
    @SequenceGenerator(
            name = "bankaccount_sequence",
            sequenceName = "bankaccount_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "bankaccount_sequence"
    )
    private Long id;
    private String accountNumber;
    private String holderName;
    private Double balance;
    private String currency;
    private Long userId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public BankAccount() {
        this.balance = 0.00;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAccountNumber() {
        this.accountNumber = UUID.randomUUID().toString();
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }
    public LocalDateTime getDeletedAt() {
        return this.deletedAt;
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getId() {
        return id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public String getHolderName() {
        return holderName;
    }

    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", holderName='" + holderName + '\'' +
                ", balance=" + balance +
                ", currency='" + currency + '\'' +
                '}';
    }
}
