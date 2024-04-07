package com.minibank.demo.bank;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.minibank.demo.MiniBankConfigProperties;
import com.minibank.demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

        String holderName = bankAccount.getHolderName();
        String currency = bankAccount.getCurrency();

        if (holderName == null || holderName == "") {
            throw new IllegalStateException("Holder name is required to continue the registration process.");
        }
        if (currency == null || currency == "") {
            throw new IllegalStateException("Currency is required to continue the registration process.");
        }

        newBankAccount.setUserId(user.getId());
        newBankAccount.setAccountNumber(); // auto-generated
        newBankAccount.setHolderName(holderName);
        newBankAccount.setCurrency(currency);
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

    public BigDecimal convertAmount(String fromCurrency, String toCurrency, BigDecimal amount) {
        BigDecimal convertedAmount = new BigDecimal("0.00");
        String uri = miniBankConfigProperties.apiUrl() +
                "/v1/convert?api_key=" +
                miniBankConfigProperties.apiKey() +
                "&from=" + fromCurrency +
                "&to=" + toCurrency +
                "&amount=" + amount.toString();

        WebClient.Builder builder = WebClient.builder();
        String response = builder.build()
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class).block();

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("API Response: " + response);
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            Double value = jsonNode.get("value").asDouble();
            System.out.println("Converted amount: " + value);
            convertedAmount = BigDecimal.valueOf(value).setScale(2, RoundingMode.DOWN);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return convertedAmount;
    }
}
