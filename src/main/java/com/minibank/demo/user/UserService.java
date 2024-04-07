package com.minibank.demo.user;

import com.minibank.demo.bank.BankAccount;
import com.minibank.demo.bank.BankAccountRepository;
import com.minibank.demo.bank.BankAccountService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private BankAccountService bankAccountService;
    @Autowired
    public UserService(
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            BankAccountRepository bankAccountRepository,
            BankAccountService bankAccountService) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountService = bankAccountService;
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

    public User getAdminUser(Long id) {
        Optional<User> userOptional = userRepository.findAdminUsers(id, true);
        if (!userOptional.isPresent()) {
            throw new IllegalStateException("Admin user does not exist");
        }

        return userOptional.get();
    }

    public void addNewUser(User user, boolean isAdmin) {
        String name = user.getName();
        String username = user.getUsername();
        if(name == null || name == "") {
            throw new IllegalStateException("Name is required to proceed with the registration");
        }
        if(username == null || username == "") {
            throw new IllegalStateException("Username is required to proceed with the registration");
        }
        if(isUserNameTaken(user.getUsername())) {
            throw new IllegalStateException("Username taken");
        }

        user.setDeletedAt(null);
        user.setCreatedAt();
        user.setUpdatedAt();
        user.setAdmin(isAdmin);
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
    public void updateUser(Long id, String name, String username, boolean isAdmin) {
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
        user.setAdmin(isAdmin);
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
    public void deposit(String accountNumber, TransactionRequest transactionRequest) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if (bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }

        DepositTransactionFactory transactionMaker = new DepositTransactionFactory();
        ITransaction transaction = transactionMaker.processTransaction();

        if (transactionRequest.getCurrency() != null && bankAccount.getCurrency() != transactionRequest.getCurrency()) {
            BigDecimal convertedAmount = bankAccountService.convertAmount(
                    transactionRequest.getCurrency(),
                    bankAccount.getCurrency(),
                    transactionRequest.getAmount());

            transaction.setAmount(convertedAmount);
        } else {
            transaction.setAmount(transactionRequest.getAmount());
        }

        transaction.setBankAccount(bankAccount);
        transaction.process();

        persistTransaction(bankAccount, transaction);
    }
    @Transactional
    public void withdraw(String accountNumber, TransactionRequest transactionRequest) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if(bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }

        WithdrawTransactionFactory transactionMaker = new WithdrawTransactionFactory();
        ITransaction transaction = transactionMaker.processTransaction();
        if (transactionRequest.getCurrency() != null && bankAccount.getCurrency() != transactionRequest.getCurrency()) {
            BigDecimal convertedAmount = bankAccountService.convertAmount(
                    transactionRequest.getCurrency(),
                    bankAccount.getCurrency(),
                    transactionRequest.getAmount());

            transaction.setAmount(convertedAmount);
        } else {
            transaction.setAmount(transactionRequest.getAmount());
        }

        transaction.setBankAccount(bankAccount);
        transaction.process();

        persistTransaction(bankAccount, transaction);
    }

    public List<Transaction> getTransactionHistory(String accountNumber, int page, int limit) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if(bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }

        Pagination pagination = new Pagination(page, limit);
        pagination.computeOffset();

        Pageable pageable = PageRequest.of(
                pagination.getPage(),
                pagination.getLimit(),
                Sort.by("timestamp").descending());

        Page<Transaction> pageableTransactions =
                transactionRepository.findTransactionsWithPagination(
                        bankAccount.getAccountNumber(),
                        pageable
                );

        List<Transaction> transactions = pageableTransactions.getContent();
        return transactions;
    }

    public Transaction getTransactionByNumber(String accountNumber, String txnNumber) {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if(bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }
        return transactionRepository.findByTxnNumber(txnNumber);
    }

    public void reverseDepositTransaction(String accountNumber, String txnNumber) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if(bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }
        if(bankAccount.isHasReversal()) {
            throw new IllegalStateException("You're not allowed to reverse another transaction until reconciliation is complete ");
        }

        Transaction currentTransaction = transactionRepository.findByTxnNumber(txnNumber);
        if(currentTransaction == null) {
            throw new IllegalStateException("Transaction does not exist");
        }
        if(currentTransaction.isHasReversed()) {
            throw new IllegalStateException("Transaction has been reversed");
        }

        DepositTransactionFactory transactionMaker = new DepositTransactionFactory();
        ITransaction transaction = transactionMaker.reverseTransaction();
        transaction.setBankAccount(bankAccount);
        transaction.setAmount(currentTransaction.getAmount());
        transaction.process();

        persistTransaction(bankAccount, transaction);

        currentTransaction.setHasReversed(true);
        transactionRepository.save(currentTransaction);
    }

    public void reverseWithdrawTransaction(String accountNumber, String txnNumber) throws Exception {
        Optional<BankAccount> bankAccountOptional = bankAccountRepository.findBankAccountByAccountNumber(accountNumber);
        BankAccount bankAccount = bankAccountOptional.get();
        if(bankAccount == null) {
            throw new IllegalStateException("Bank account number does not exist");
        }
        if(bankAccount.isHasReversal()) {
            throw new IllegalStateException("You're not allowed to reverse another transaction until reconciliation is complete ");
        }

        Transaction currentTransaction = transactionRepository.findByTxnNumber(txnNumber);
        if(currentTransaction == null) {
            throw new IllegalStateException("Transaction does not exist");
        }
        if(currentTransaction.isHasReversed()) {
            throw new IllegalStateException("Transaction has been reversed");
        }

        WithdrawTransactionFactory transactionMaker = new WithdrawTransactionFactory();
        ITransaction transaction = transactionMaker.reverseTransaction();
        transaction.setBankAccount(bankAccount);
        transaction.setAmount(currentTransaction.getAmount());
        transaction.process();

        persistTransaction(bankAccount, transaction);

        currentTransaction.setHasReversed(true);
        transactionRepository.save(currentTransaction);
    }

    private void persistTransaction(BankAccount bankAccount, ITransaction transaction) {
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.getAmount());
        newTransaction.setBankAccountNumber(bankAccount.getAccountNumber());
        newTransaction.setType(transaction.getType());
        newTransaction.setTimestamp(transaction.getTimestamp());
        newTransaction.setCreatedAt();
        newTransaction.setUpdatedAt();
        transactionRepository.save(newTransaction);
    }

    public void checkBankAccountsByUserId(Long userId) {
//        Optional<BankAccount> bankAccountsOptional = bankAccountRepository.findBankAccountByUserId(userId);
//        if(bankAccountsOptional.isPresent()) {
//            throw new IllegalStateException("Cannot delete user due to existing bank accounts linked to the user account");
//        }
        Optional<User> userOptional = userRepository.findAdminUsers(userId, true);
        if (userOptional.isPresent()) {
            throw new IllegalStateException("Cannot delete admin user");
        }
    }
}
