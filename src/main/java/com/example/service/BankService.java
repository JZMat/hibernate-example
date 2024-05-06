package com.example.service;

import com.example.model.Bank;

import java.util.List;

// Bank service interface
public interface BankService {

    // Method to save a bank
    void saveBank(Bank bank);

    // Method to list all banks
    List<Bank> getAllBanks();

}
