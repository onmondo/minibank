package com.minibank.demo.user;

public class WithdrawTransactionFactory {
    protected ITransaction processTransaction() {
        ITransaction transaction = new WithdrawITransaction("withdraw");
        return transaction;
    }
}
