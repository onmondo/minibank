package com.minibank.demo.user;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String name;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    private boolean isAdmin;
    @Transient
    private List<BankAccount> bankAccounts;

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setUsername(String username) { this.username = username; }

    public String getUsername() { return this.username; }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
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

    @Autowired
    public User() {
        this.bankAccounts = new ArrayList<>();
        this.isAdmin = false;
    }
    public BankAccount createAccount() {
        BankAccount bankAccount = new BankAccount();
        this.bankAccounts.add(bankAccount);
        return bankAccount;
    }

    public BigDecimal getBalance(BankAccount bankAccount) {
        BigDecimal currentBalance = bankAccount.getBalance();
        return currentBalance;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", bankAccounts=" + bankAccounts +
                ", isAdmin=" + isAdmin +
                '}';
    }
}
