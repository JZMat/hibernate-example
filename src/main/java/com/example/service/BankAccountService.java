package com.example.service;

import com.example.model.BankAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BankAccountService {
    void saveBankAccount(BankAccount bankAccount);

    List<BankAccount> getAllBankAccounts();

    void updateBankAccount(BankAccount chosenAccount);

    BigDecimal getTotalBalanceOfAllAccountsByOwner();

    Map<String, Map<String, BigDecimal>> getBalanceForEachOwnerByBankAndAccount();

    Map<String, Map<String, BigDecimal>> getTotalBalanceForEachOwnerByBank();

    Map<String, BigDecimal> getTotalBalanceForEachOwner();
}
