package com.minibank.demo.bank;

import com.minibank.demo.MiniBankConfigProperties;
import com.minibank.demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final MiniBankConfigProperties miniBankConfigProperties;

    @Autowired
    public BankAccountService(BankAccountRepository bankAccountRepository, MiniBankConfigProperties miniBankConfigProperties) {
        this.bankAccountRepository = bankAccountRepository;
        this.miniBankConfigProperties = miniBankConfigProperties;
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

    public void markBankAccountReversal(String accountNumber) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        bankAccount.setHasReversal(true);

        bankAccountRepository.save(bankAccount);
    }

    public void reconcileBankAccount(String accountNumber) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        bankAccount.setHasReversal(false);

        bankAccountRepository.save(bankAccount);
    }

    public List<String> fetchCurrencies() {
        Bank bankInstance = Bank.getInstance(miniBankConfigProperties);
        return bankInstance.getAvailableCurrencies();
    }
}
