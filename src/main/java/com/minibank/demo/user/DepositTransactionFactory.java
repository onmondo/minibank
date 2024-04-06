package com.minibank.demo.user;

public class DepositTransactionFactory {
    protected ITransaction processTransaction() {
        ITransaction transaction = new DepositITransaction("deposit");
        return transaction;
    }
}
