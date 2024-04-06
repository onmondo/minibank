package com.minibank.demo.user;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table
public class Transaction {
    @Id
    @SequenceGenerator(
            name = "txn_sequence",
            sequenceName = "txn_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "txn_sequence"
    )
    private Long id;
    private String txnNumber;
    private LocalDateTime timestamp;
    private String bankAccountNumber;
    private BigDecimal amount;
    private String type;
    private boolean hasReversed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public Transaction() {
        this.txnNumber = UUID.randomUUID().toString();
        this.hasReversed = false;
    }
    public void setType(String type) {
        this.type = type;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

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

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getTxnNumber() {
        return txnNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isHasReversed() {
        return hasReversed;
    }

    public void setHasReversed(boolean hasReversed) {
        this.hasReversed = hasReversed;
    }
}
