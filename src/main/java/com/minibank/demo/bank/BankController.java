package com.minibank.demo.bank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/bank")
public class BankController {
    private BankAccountService bankAccountService;
    @Autowired
    public BankController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
    @GetMapping(path = "currencies")
    public List<String> getCurrencies() {
        return bankAccountService.fetchCurrencies();
    }
}
