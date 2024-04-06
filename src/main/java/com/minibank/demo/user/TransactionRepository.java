package com.minibank.demo.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.txnNumber = :txnNumber")
    Transaction findByTxnNumber(@Param("txnNumber") String txnNumber);
    @Query("SELECT t FROM Transaction t WHERE t.bankAccountNumber = :bankAccountNumber")
    Page<Transaction> findTransactionsWithPagination(
            @Param("bankAccountNumber") String bankAccountNumber,
            Pageable pageable
    );
}
