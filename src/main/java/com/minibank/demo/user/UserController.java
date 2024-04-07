package com.minibank.demo.user;

import com.minibank.demo.bank.BankAccount;
import com.minibank.demo.bank.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private UserService userService;
    private BankAccountService bankAccountService;
    @Autowired
    public UserController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }
    @GetMapping
    public List<User> getUsers() throws Exception {
        return this.userService.getUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable("userId") Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void registerNewUser(
            @RequestBody User user,
            @RequestParam(required = false) boolean isAdmin) {
        userService.addNewUser(user, isAdmin);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.checkBankAccountsByUserId(id);
        userService.deleteUser(id);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Long id,
            @RequestParam(required = false) boolean isAdmin,
            @RequestBody(required = false) User user) {
        System.out.println(isAdmin);
        userService.updateUser(id, user.getName(), user.getUsername(), isAdmin);
    }

    @PostMapping(path = "{userId}/bankaccounts")
    public void registerBankAccount(
            @PathVariable("userId") Long id,
            @RequestBody BankAccount bankAccount) {
        User user = userService.getUser(id);
        bankAccountService.addNewBankAccount(user, bankAccount);
    }

    @GetMapping(path = "{userId}/bankaccounts")
    public List<BankAccount> getUsersBankAccounts(@PathVariable("userId") Long id) {
        User user = userService.getUser(id);
        return bankAccountService.getUsersBankAccounts(user.getName());
    }

    @GetMapping(path = "{userId}/bankaccounts/{bankAccountNumber}")
    public BankAccount getUsersBankAccount(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber) {
        userService.getUser(id);
        return bankAccountService.getUsersBankAccount(accountNumber);
    }

    @PostMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/deposit")
    public void depositFund(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestBody TransactionRequest transactionRequest) throws Exception {
        userService.getUser(id);
        userService.deposit(accountNumber, transactionRequest.getAmount());
    }

    @PostMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/withdraw")
    public void withdrawFund(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestBody TransactionRequest transactionRequest) throws Exception {
        userService.getUser(id);
        userService.withdraw(accountNumber, transactionRequest.getAmount());
    }

    @GetMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/transactions")
    public List<Transaction> getUserTransactions(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestParam(required = false) int page,
            @RequestParam(required = false) int limit) {
        userService.getUser(id);
        return userService.getTransactionHistory(accountNumber, page, limit);
    }

    @GetMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/transactions/{transactionId}")
    public Transaction getUserTransaction(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @PathVariable("transactionId") String transactionId) {
        userService.getUser(id);
        return userService.getTransactionByNumber(accountNumber, transactionId);
    }

    @DeleteMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/transactions/{transactionId}/deposit")
    public void reverseDepositUserTransaction(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @PathVariable("transactionId") String transactionId) throws Exception {
        userService.getUser(id);
        userService.reverseDepositTransaction(accountNumber, transactionId);
        bankAccountService.markBankAccountReversal(accountNumber);
    }

    @DeleteMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/transactions/{transactionId}/withdraw")
    public void reverseWithdrawUserTransaction(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @PathVariable("transactionId") String transactionId) throws Exception {
        userService.getUser(id);
        userService.reverseWithdrawTransaction(accountNumber, transactionId);
        bankAccountService.markBankAccountReversal(accountNumber);
    }

    @PostMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/reconcile")
    public void reconcileBankAccount(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestParam(required = false) Long adminId) {
        userService.getUser(id);
        User user = userService.getAdminUser(adminId);
        System.out.println(user);
        bankAccountService.reconcileBankAccount(accountNumber);
    }

}
