package com.example.repository;

import com.example.model.BankAccount;

import java.util.List;

public interface BankAccountRepository {
    void saveBankAccount(BankAccount bankAccount);

    List<BankAccount> getAllBankAccounts();
}
