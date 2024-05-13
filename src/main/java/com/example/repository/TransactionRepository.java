package com.example.repository;

import com.example.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    Transaction getTransaction(int transactionId);
    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactionsByBankAccount(int accountId);
}
