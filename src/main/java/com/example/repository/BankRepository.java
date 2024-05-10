package com.example.repository;

import com.example.model.Bank;

import java.util.List;

public interface BankRepository {

    void saveBank(Bank bank);
    /**
     * Retrieves a list of all banks from the database.
     *
     * @return A list containing all banks stored in the database.
     */
    List<Bank> getAllBanks();
}
