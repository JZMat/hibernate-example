package com.example.service;

import com.example.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    void saveBankAccount(BankAccount bankAccount);

    List<BankAccount> getAllBankAccounts();
}
