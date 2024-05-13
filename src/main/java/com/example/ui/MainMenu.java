// Package declaration
package com.example.ui;

// Import statements

import com.example.service.BankAccountService;
import com.example.service.BankService;
import com.example.service.OwnerService;
import com.example.service.TransactionService;
import com.example.util.InputUtils;

// Main menu class
public class MainMenu extends TextMenu {
    // Instance variable for bank service
    private final BankService bankService;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;
    private boolean exitRequested = false;

    // Constructor
    public MainMenu(BankService bankService, OwnerService ownerService, BankAccountService bankAccountService, TransactionService transactionService) {

        this.bankService = bankService;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
    }

    // Override method to display the main menu
    @Override
    public void displayMenu() {
        System.out.println("Main Menu:");
        printOption("1", "Bank Management");
        printOption("2", "Owner Management");
        printOption("3", "Bank Account Management");
        printOption("4", "Deposit Management");
        printOption("5", "View Balance Sheets");
        printOption("6", "Transaction History");
        printOption("7", "Exit");

    }

    // Override method to handle user input
    @Override
    public void handleUserInput() {

        int choice = InputUtils.getValidatedInput("Enter your choice: ", 1, 7);
        switch (choice) {
            case 1:
                // Code to navigate to BankMenu
                BankMenu bankMenu = new BankMenu(bankService);
                bankMenu.run();
                break;
            case 2:
                // Code to navigate to OwnerMenu
                OwnerMenu ownerMenu = new OwnerMenu(ownerService);
                ownerMenu.run();
                break;
            case 3:
                // Code to navigate to BankAccountMenu
                BankAccountMenu bankAccountMenu = new BankAccountMenu(bankAccountService, ownerService, bankService, transactionService);
                bankAccountMenu.run();
                break;
            case 4:
                //  Code to navigate to sth.
                System.out.println("Not implemented yet.");
                break;
            case 5:
                // Code to navigate to BalanceSheetMenu
                BalanceSheetMenu balanceSheetMenu = new BalanceSheetMenu(bankAccountService);
                balanceSheetMenu.run();
                break;
            case 6:
                // Code to navigate to TransactionHistoryMenu
                TransactionHistoryMenu transactionHistoryMenu = new TransactionHistoryMenu(transactionService);
                transactionHistoryMenu.run();
                break;
            case 7:
                System.out.println("Exiting...");
                exitRequested = true; // Set exit flag
                return; // Return from the method
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void run() {
        while (!exitRequested) { // Check exit flag
            displayMenu();
            handleUserInput();
        }
    }

}

