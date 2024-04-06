package com.minibank.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long>  {
    @Query("SELECT b FROM BankAccount b WHERE b.holderName = ?1")
    Optional<List<BankAccount>> findBankAccountByUserName(String name);
    @Query("SELECT b FROM BankAccount b WHERE b.accountNumber = ?1")
    Optional<BankAccount> findBankAccountByAccountNumber(String accountNumber);
}
