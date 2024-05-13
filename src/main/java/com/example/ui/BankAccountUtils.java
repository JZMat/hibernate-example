package com.example.ui;

import com.example.model.BankAccount;
import com.example.model.Owner;

import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class BankAccountUtils {
    public static void displayFormattedBankAccounts(List<BankAccount> bankAccounts) {

        if (bankAccounts.isEmpty()) {
            System.out.println(MenuConstants.NO_BANK_ACCOUNTS_FOUND);
        } else {

            List<String> accountNames = bankAccounts.stream()
                    .map(BankAccount::getAccount_name)
                    .collect(Collectors.toList());
            int maxAcctNameLen = Math.max(findLongestStringLength(accountNames), MenuConstants.ACCOUNT_HEADER.length());

            List<String> bankNames = bankAccounts.stream()
                    .map(bankAccount -> bankAccount.getBank().getBank_name())
                    .collect(Collectors.toList());
            int maxBankNameLen = Math.max(findLongestStringLength(bankNames), MenuConstants.BANK_HEADER.length());

            List<String> balances = bankAccounts.stream()
                    .map(bankAccount -> bankAccount.getBalance().toString())
                    .collect(Collectors.toList());
            int maxBalanceLen = Math.max(findLongestStringLength(balances), MenuConstants.BALANCE_HEADER.length());

            List<String> ownersNames = bankAccounts.stream()
                    .map(account -> {
                        Set<Owner> owners = account.getOwners();
                        StringJoiner ownersNamesJoiner = new StringJoiner(", ");
                        for (Owner owner : owners) {
                            ownersNamesJoiner.add(owner.getOwner_name());
                        }
                        return ownersNamesJoiner.toString();
                    })
                    .collect(Collectors.toList());

            int maxOwnersNamesLen = Math.max(findLongestStringLength(ownersNames), MenuConstants.OWNERS_HEADER.length());

            int totalLen = MenuConstants.INDEX_HEADER.length() + maxAcctNameLen + maxBalanceLen + maxBankNameLen + maxOwnersNamesLen
                    + " | ".length() * 4;
            String separator = "-".repeat(totalLen);

            System.out.println("maxAcctNameLen: " + maxAcctNameLen);
            System.out.println("maxBalanceLen: " + maxBalanceLen);
            System.out.println("maxBankNameLen: " + maxBankNameLen);
            System.out.println("maxOwnersNamesLen: " + maxOwnersNamesLen);
            System.out.println("totalLen: " + totalLen);

            for (int i = 0; i < bankAccounts.size(); i++) {
                System.out.println(formatBankAccountAndHeader(separator, i + 1, bankAccounts.get(i), maxAcctNameLen, maxBalanceLen,
                        maxBankNameLen, maxOwnersNamesLen));
            }
        }
    }

    private static int findLongestStringLength(List<String> strings) {
        return strings.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    private static String formatBankAccountAndHeader(String separator, int index, BankAccount bankAccount, int maxAccountNameLen,
                                              int maxBalanceLen, int maxBankNameLen, int maxOwnerNameLen) {
        String accountColWidthSpecifier = "%-" + (maxAccountNameLen) + "s";
        String bankColWidthSpecifier = "%-" + (maxBankNameLen) + "s";
        String balanceColWidthSpecifier = "%-" + (maxBalanceLen) + ".2f";
        String balanceHeaderColWidthSpecifier = "%-" + (maxBalanceLen) + "s";
        String ownersColWidthSpecifier = "%-" + (maxOwnerNameLen) + "s\n";


        if (index == 1) {
            return String.format("\nList of all bank accounts:\n" +
                            separator + "\n" +
                            "Index | " + accountColWidthSpecifier + " | " + balanceHeaderColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier +
                            separator + "\n" +
                            "%-5d | " + accountColWidthSpecifier + " | " + balanceColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier,
                    MenuConstants.ACCOUNT_HEADER, MenuConstants.BALANCE_HEADER, MenuConstants.BANK_HEADER, MenuConstants.OWNERS_HEADER,
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        } else {
            return String.format("%-5d | " + accountColWidthSpecifier + " | " + balanceColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier,
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        }
    }

    private static String getOwnersAsString(Set<Owner> owners) {
        if (owners.isEmpty()) {
            return "None";
        }
        StringBuilder ownerNames = new StringBuilder();
        for (Owner owner : owners) {
            ownerNames.append(owner.getOwner_name()).append(", ");
        }
        // Remove the trailing comma and space
        ownerNames.setLength(ownerNames.length() - 2);
        return ownerNames.toString();
    }
}
