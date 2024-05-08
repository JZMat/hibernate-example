// Package declaration
package com.example.ui;

// Import statements

import com.example.service.BankAccountService;
import com.example.service.BankService;
import com.example.service.OwnerService;
import com.example.util.InputUtils;

import java.util.InputMismatchException;

// Main menu class
public class MainMenu extends TextMenu {
    // Instance variable for bank service
    private final BankService bankService;
    private final OwnerService ownerService;
    private final BankAccountService bankAccountService;
    private boolean exitRequested = false;

    // Constructor
    public MainMenu(BankService bankService, OwnerService ownerService, BankAccountService bankAccountService) {

        this.bankService = bankService;
        this.ownerService = ownerService;
        this.bankAccountService = bankAccountService;
    }

    // Override method to display the main menu
    @Override
    public void displayMenu() {
        System.out.println("Main Menu:");
        printOption("1", "Bank Management");
        printOption("2", "Owner Management");
        printOption("3", "Bank Account Management");
        printOption("4", "Deposit Management");
        printOption("5", "Exit");
    }

    // Override method to handle user input
    @Override
    public void handleUserInput() {
        try {
            int choice = InputUtils.getIntInput("Enter your choice: ");
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
                    BankAccountMenu bankAccountMenu = new BankAccountMenu(bankAccountService, ownerService, bankService);
                    bankAccountMenu.run();
                    break;
                // case 4:
                //
                //break;
                case 5:
                    System.out.println("Exiting...");
                    exitRequested = true; // Set exit flag
                    return; // Return from the method
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter a number.");
        }

    }

    public void run() {
        while (!exitRequested) { // Check exit flag
            displayMenu();
            handleUserInput();
        }
    }

}
