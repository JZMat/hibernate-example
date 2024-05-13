package com.example.service;

import com.example.model.BankAccount;
import com.example.model.Transaction;
import com.example.repository.BankAccountRepository;
import com.example.repository.TransactionRepository;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        return transactionRepository.getTransaction(transactionId);
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        transactionRepository.saveTransaction(transaction);
    }

    @Override
    public List<Transaction> getTransactionHistory(BankAccount chosenAccount) {
        return transactionRepository.getTransactionsByBankAccount(chosenAccount.getAccountId());
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.getAllBankAccounts();
    }
}
