package com.minibank.demo.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public UserService(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    public List<User> getActiveUsers() {
        Optional<User> usersOptional = this.userRepository.findActiveUsers();
        if (!usersOptional.isPresent()) {
            throw new IllegalStateException("Empty users");
        }
        return (List<User>) usersOptional.get();
    }

    public User getUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new IllegalStateException("User does not exist");
        }

        return userOptional.get();
    }

    public void addNewUser(User user) {
        if(isUserNameTaken(user.getUsername())) {
            throw new IllegalStateException("Username taken");
        }
        user.setDeletedAt(null);
        user.setCreatedAt();
        user.setUpdatedAt();
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exist");
        }

        userRepository.deleteById(id);
    }
    @Transactional
    public void updateUser(Long id, String name, String username) {
        boolean exists = userRepository.existsById(id);
        if (!exists) {
            throw new IllegalStateException("User with id " + id + " does not exist");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(
                        "User with id " + id + " does not exist"));
        user.setId(id);
        if (name != null &&
                name.length() > 0 &&
                !Objects.equals(user.getName(), name)) {
            user.setName(name);
        }
        if (username != null &&
                username.length() > 0 &&
                !Objects.equals(user.getUsername(), username)) {
            if(isUserNameTaken(username)) {
                throw new IllegalStateException("Username taken");
            }
            user.setUsername(username);
        }
        user.setUpdatedAt();
        userRepository.save(user);
    }

    private boolean isUserNameTaken(String username) {
        Optional<User> userOptional = userRepository.findUserByUserName(username);

        if(userOptional.isPresent()) {
            return true;
        }

        return false;
    }
    @Transactional
    public void deposit(String accountNumber, Double amount) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();

        DepositTransactionFactory transactionMaker = new DepositTransactionFactory();
        ITransaction transaction = transactionMaker.processTransaction();
        transaction.setBankAccount(bankAccount);
        transaction.setAmount(amount);
        transaction.process();

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setBankAccountNumber(bankAccount.getAccountNumber());
        newTransaction.setType(transaction.getType());
        newTransaction.setTimestamp(transaction.getTimestamp());
        newTransaction.setCreatedAt();
        newTransaction.setUpdatedAt();
        transactionRepository.save(newTransaction);
    }
    @Transactional
    public void withdraw(String accountNumber, Double amount) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();

        WithdrawTransactionFactory transactionMaker = new WithdrawTransactionFactory();
        ITransaction transaction = transactionMaker.processTransaction();
        transaction.setBankAccount(bankAccount);
        transaction.setAmount(amount);
        transaction.process();

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setBankAccountNumber(bankAccount.getAccountNumber());
        newTransaction.setType(transaction.getType());
        newTransaction.setTimestamp(transaction.getTimestamp());
        newTransaction.setCreatedAt();
        newTransaction.setUpdatedAt();
        transactionRepository.save(newTransaction);
    }
}
