package com.example.service;

import com.example.model.BankAccount;
import com.example.repository.BankAccountRepository;

import java.util.List;

public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void saveBankAccount(BankAccount bankAccount) {
        bankAccountRepository.saveBankAccount(bankAccount);
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.getAllBankAccounts();
    }

    @Override
    public void updateBankAccount(BankAccount chosenAccount) {
        bankAccountRepository.updateBankAccount(chosenAccount);
    }
}
