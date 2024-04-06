package com.minibank.demo.user;

public abstract class TransactionFactory {
    public ITransaction createTransaction() throws Exception {
        ITransaction transaction = this.processTransaction();
        transaction.process();
        return transaction;
    }

    protected abstract ITransaction processTransaction();
}
