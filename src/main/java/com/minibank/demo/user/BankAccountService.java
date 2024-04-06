package com.minibank.demo.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<BankAccount> getBankAccounts() {
        return this.bankAccountRepository.findAll();
    }

    public void addNewBankAccount(User user, BankAccount bankAccount) {
        BankAccount newBankAccount = user.createAccount();

        newBankAccount.setUserId(user.getId());
        newBankAccount.setAccountNumber(); // auto-generated
        newBankAccount.setHolderName(bankAccount.getHolderName());
        newBankAccount.setCurrency(bankAccount.getCurrency());
        newBankAccount.setDeletedAt(null);
        newBankAccount.setCreatedAt();
        newBankAccount.setUpdatedAt();
        bankAccountRepository.save(newBankAccount);
    }

    public List<BankAccount> getUsersBankAccounts(String name) {
        Optional<List<BankAccount>> bankAccountsOptional = bankAccountRepository.findBankAccountByUserName(name);
        return bankAccountsOptional.get();
    }

    public BankAccount getUsersBankAccount(String accountNumber) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        return bankAccountOptional.get();
    }

//    public void depositFunds(User user, String accountNumber, Double funds) throws Exception {
//        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
//        BankAccount bankAccount = bankAccountOptional.get();
//
//        user.deposit(bankAccount, funds);
//    }
//
//    public void withdrawFunds(User user, String accountNumber, Double funds) throws Exception {
//        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
//        BankAccount bankAccount = bankAccountOptional.get();
//
//        user.withdraw(bankAccount, funds);
//    }
}
