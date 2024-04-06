package com.minibank.demo.user;

public class DepositTransactionFactory {
    protected ITransaction processTransaction() {
        ITransaction transaction = new DepositITransaction("deposit");
        return transaction;
    }

    protected  ITransaction reverseTransaction() {
        ITransaction transaction = new ReverseDepositITransaction("depositreversal");
        return transaction;
    }
}
