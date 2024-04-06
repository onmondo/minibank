package com.minibank.demo.user;

public class WithdrawTransactionFactory {
    protected ITransaction processTransaction() {
        ITransaction transaction = new WithdrawITransaction("withdraw");
        return transaction;
    }

    protected  ITransaction reverseTransaction() {
        ITransaction transaction = new ReverseDepositITransaction("withdrawreversal");
        return transaction;
    }
}
