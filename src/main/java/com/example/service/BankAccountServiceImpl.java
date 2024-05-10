package com.example.service;

import com.example.model.BankAccount;
import com.example.model.Owner;
import com.example.repository.BankAccountRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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

    // In BankAccountServiceImpl.java

    @Override
    public BigDecimal getTotalBalanceOfAllAccountsByOwner() {
        BigDecimal totalBalance = BigDecimal.ZERO;
        List<BankAccount> allAccounts = getAllBankAccounts();
        for (BankAccount account : allAccounts) {
            totalBalance = totalBalance.add(account.getBalance());
        }
        return totalBalance;
    }

    @Override
    public Map<String, Map<String, BigDecimal>> getBalanceForEachOwnerByBankAndAccount() {
        Map<String, Map<String, BigDecimal>> balanceForEachOwner = new HashMap<>();
        List<BankAccount> allAccounts = getAllBankAccounts();
        for (BankAccount account : allAccounts) {
            String bankName = account.getBank().getBank_name();
            String accountName = account.getAccount_name();
            BigDecimal balance = account.getBalance();
            Set<Owner> owners = account.getOwners();
            String ownerNames = owners.stream().map(Owner::getOwner_name).collect(Collectors.joining(", "));
            balanceForEachOwner.putIfAbsent(ownerNames, new HashMap<>());
            balanceForEachOwner.get(ownerNames).put(bankName + " - " + accountName, balance);
        }
        return balanceForEachOwner;
    }

    @Override
    public Map<String, Map<String, BigDecimal>> getTotalBalanceForEachOwnerByBank() {
        Map<String, Map<String, BigDecimal>> totalBalanceForEachOwnerByBank = new HashMap<>();
        List<BankAccount> allAccounts = getAllBankAccounts();
        for (BankAccount account : allAccounts) {
            String bankName = account.getBank().getBank_name();
            BigDecimal balance = account.getBalance();
            Set<Owner> owners = account.getOwners();
            String ownerNames = owners.stream().map(Owner::getOwner_name).collect(Collectors.joining(", "));
            totalBalanceForEachOwnerByBank.putIfAbsent(ownerNames, new HashMap<>());
            totalBalanceForEachOwnerByBank.get(ownerNames).put(bankName, totalBalanceForEachOwnerByBank.get(ownerNames).getOrDefault(bankName, BigDecimal.ZERO).add(balance));
        }
        return totalBalanceForEachOwnerByBank;
    }


    @Override
    public Map<String, BigDecimal> getTotalBalanceForEachOwner() {
        Map<String, BigDecimal> totalBalanceForEachOwner = new HashMap<>();
        List<BankAccount> allAccounts = getAllBankAccounts();
        for (BankAccount account : allAccounts) {
            BigDecimal balance = account.getBalance();
            Set<Owner> owners = account.getOwners();
            String ownerNames = owners.stream().map(Owner::getOwner_name).collect(Collectors.joining(", "));
            totalBalanceForEachOwner.put(ownerNames, totalBalanceForEachOwner.getOrDefault(ownerNames, BigDecimal.ZERO).add(balance));
        }
        return totalBalanceForEachOwner;
    }

}
