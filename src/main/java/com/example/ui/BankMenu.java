package com.example.ui;

import com.example.model.Bank;
import com.example.service.BankService;
import com.example.util.InputUtils;

import java.util.InputMismatchException;
import java.util.List;

public class BankMenu extends TextMenu{
    // Instance variable for bank service
    private final BankService bankService;
    private boolean returnToMainMenu = false;

    // Constructor
    public BankMenu(BankService bankService) {
        this.bankService = bankService;
    }

    // Override method to display the bank menu
    @Override
    public void displayMenu() {
        System.out.println("Bank Management Menu:");
        printOption("1", "Create a new bank");
        printOption("2", "View all banks");
        printOption("3", "Update bank details");
        printOption("4", "Delete a bank");
        printOption("5", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {
        try {
            int choice = InputUtils.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewBank();
                    break;
                case 2:
                    viewAllBanks();
                    break;
                case 3:
                    // updateBankDetails();
                    break;
                case 4:
                    // deleteBank();
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    returnToMainMenu = true; // Set flag to true
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter a number.");
        }
    }

    // Method to create a new bank
    private void createNewBank() {
        String bankName = InputUtils.getStringInput("Enter the name of the new bank: ");
        Bank bank = new Bank(bankName);
        bankService.saveBank(bank);
        System.out.println("Bank '" + bankName + "' created successfully!");
    }

    // Method to view all banks
    private void viewAllBanks() {
        List<Bank> banks = bankService.getAllBanks();
        if (banks.isEmpty()) {
            System.out.println("No banks found.");
        } else {
            System.out.println("List of all banks:");
            for (Bank bank : banks) {
                System.out.println(bank.getBank_name());
            }
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
