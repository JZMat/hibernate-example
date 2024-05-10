// Package declaration
package com.example.ui;

// Import statements

// Abstract text menu class
public abstract class TextMenu {
    // Abstract method to display the menu
    public abstract void displayMenu();

    // Abstract method to handle user input
    public abstract void handleUserInput();

     /**
     * This method prints a menu option to the console.
     *
     * @param optionNumber The number of the option in the menu.
     * @param optionDescription A description of what the option does.
     */
    protected void printOption(String optionNumber, String optionDescription) {

        try {
            // Print the option number and description to the console
            System.out.println(optionNumber + ". " + optionDescription);
        } catch (Exception e) {
            // If an error occurs, print an error message
            System.out.println("An error occurred while trying to print the option: " + e.getMessage());
            // TODO
        }
    }
}

