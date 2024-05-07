// Package declaration
package com.example.ui;

// Import statements
import com.example.service.BankService;
import com.example.service.BankServiceImpl;
import com.example.util.InputUtils;

// Abstract text menu class
public abstract class TextMenu {
    // Abstract method to display the menu
    public abstract void displayMenu();

    // Abstract method to handle user input
    public abstract void handleUserInput();

    // Protected method to print menu options
    protected void printOption(String option, String description) {

        System.out.println(option + ". " + description);
    }
}

