package com.example.ui;

import com.example.service.BankAccountService;
import com.example.util.InputUtils;

import java.math.BigDecimal;
import java.util.Map;

public class BalanceSheetMenu extends TextMenu {
    private final BankAccountService bankAccountService;
    private boolean returnToMainMenu = false;
    private BigDecimal totalBalance;

    public BalanceSheetMenu(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Balance Sheet Menu:");
        printOption("1", "View total balance by owner");
        printOption("2", "View balance for each owner by bank and account");
        printOption("3", "View total balance");
        printOption("4", "View balance for each owner by bank");
        printOption("5", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {
        int choice = InputUtils.getValidatedInput("Enter your choice: ", 1, 5);
        switch (choice) {
            case 1:
                // Code to display total balance of all accounts by owner
                Map<String, BigDecimal> totalBalanceForEachOwner = bankAccountService.getTotalBalanceForEachOwner();
                totalBalance = BigDecimal.ZERO;
                System.out.println("Total balance of all accounts by owner: ");
                for (Map.Entry<String, BigDecimal> entry : totalBalanceForEachOwner.entrySet()) {
                    String ownerName = entry.getKey();
                    BigDecimal balance = entry.getValue();
                    totalBalance = totalBalance.add(balance);
                    System.out.println("    " + ownerName + ": " + balance);
                }
                System.out.println("--------------------------------");
                System.out.println("Total: " + totalBalance);
                break;

            case 2:

                // Code to display balance for each owner by bank and account
                Map<String, Map<String, BigDecimal>> balanceForEachOwner = bankAccountService.getBalanceForEachOwnerByBankAndAccount();
                for (Map.Entry<String, Map<String, BigDecimal>> entry : balanceForEachOwner.entrySet()) {
                    String ownerNames = entry.getKey();
                    Map<String, BigDecimal> balances = entry.getValue();
                    if (ownerNames.contains(",")) {
                        System.out.println("Owners: " + ownerNames);
                    } else {
                        System.out.println("Owner: " + ownerNames);
                    }
                    for (Map.Entry<String, BigDecimal> balanceEntry : balances.entrySet()) {
                        String accountInfo = balanceEntry.getKey();
                        BigDecimal balance = balanceEntry.getValue();
                        System.out.println("    " + accountInfo + ": " + balance);
                    }
                }
                break;
            case 3:
                // Code to display total balance
                totalBalance = bankAccountService.getTotalBalanceOfAllAccountsByOwner();
                System.out.println("Total balance : " + totalBalance);
                break;
            case 4:
                // Code to display total balance by owner and bank
                Map<String, Map<String, BigDecimal>> totalBalanceForEachOwnerByBank = bankAccountService.getTotalBalanceForEachOwnerByBank();
                for (Map.Entry<String, Map<String, BigDecimal>> entry : totalBalanceForEachOwnerByBank.entrySet()) {
                    String ownerNames = entry.getKey();
                    Map<String, BigDecimal> balances = entry.getValue();
                    if (ownerNames.contains(",")) {
                        System.out.println("Owners: " + ownerNames);
                    } else {
                        System.out.println("Owner: " + ownerNames);
                    }
                    for (Map.Entry<String, BigDecimal> balanceEntry : balances.entrySet()) {
                        String bankName = balanceEntry.getKey();
                        BigDecimal balance = balanceEntry.getValue();
                        // System.out.println("    " + bankName + ": " + balance);
                        System.out.printf("    %-15s: %10.2f%n", bankName, balance);
                    }
                }
                break;

            case 5:
                System.out.println("Returning to main menu...");
                returnToMainMenu = true;
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void run() {
        while (!returnToMainMenu) { // Continue until flag is true
            displayMenu();
            handleUserInput();
        }
        returnToMainMenu = false; // Reset flag for next iteration
    }
}

