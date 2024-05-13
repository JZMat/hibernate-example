package com.example.service;

import com.example.model.BankAccount;
import com.example.model.Transaction;
import java.util.List;

public interface TransactionService {
    Transaction getTransaction(int transactionId);
    void saveTransaction(Transaction transaction);
    List<Transaction> getTransactionHistory(BankAccount chosenAccount);
    List<BankAccount> getAllBankAccounts();
}
