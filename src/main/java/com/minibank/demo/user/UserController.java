package com.minibank.demo.user;

import jakarta.transaction.Transactional;
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
//        User newUser = new User();
//        newUser.setId(1l);
//        newUser.setName("John Raymond");
//        BankAccount newBankAccount = newUser.createAccount();
//        newBankAccount.setId(7893922l);
//        newBankAccount.setBalance(1000.00);
//        newBankAccount.setAccountNumber("73fh73b87s");
//        newBankAccount.setHolderName(newUser.getName());
//        newBankAccount.setCurrency("PHP");
//        newUser.deposit(newBankAccount, 1500.00);
//        return List.of(newUser.toString());

        return this.userService.getUsers();
    }

    @GetMapping(path = "{userId}")
    public User getUser(@PathVariable("userId") Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public void registerNewUser(@RequestBody User user) {
        userService.addNewUser(user);
    }

    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") Long id) {
        userService.deleteUser(id);
    }

    @PutMapping(path = "{userId}")
    public void updateUser(
            @PathVariable("userId") Long id,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String username,
            @RequestBody(required = false) User user) {
        System.out.println(user.getName() + user.getUsername());
        userService.updateUser(id, user.getName(), user.getUsername());
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
        User user = userService.getUser(id);
        return bankAccountService.getUsersBankAccount(accountNumber);
    }

    @PostMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/deposit")
    public void depositFund(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestBody TransactionRequest transactionRequest) throws Exception {
        User user = userService.getUser(id);
        userService.deposit(accountNumber, transactionRequest.getAmount());
    }

    @PostMapping(path = "{userId}/bankaccounts/{bankAccountNumber}/withdraw")
    public void withdrawFund(
            @PathVariable("userId") Long id,
            @PathVariable("bankAccountNumber") String accountNumber,
            @RequestBody TransactionRequest transactionRequest) throws Exception {
        User user = userService.getUser(id);
        userService.withdraw(accountNumber, transactionRequest.getAmount());
    }
}
