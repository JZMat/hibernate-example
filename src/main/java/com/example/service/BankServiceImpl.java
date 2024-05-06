package com.example.service;

import com.example.model.Bank;
import com.example.repository.BankRepository;

import java.util.List;

public class BankServiceImpl implements BankService {
    private final BankRepository bankRepository;

    public BankServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public void saveBank(Bank bank) {
        bankRepository.saveBank(bank);
    }

    @Override
    public List<Bank> getAllBanks() {
        return bankRepository.getAllBanks();
    }
}
