package com.example.ui;

import com.example.model.Owner;
import com.example.service.OwnerService;
import com.example.util.InputUtils;

import java.util.InputMismatchException;
import java.util.List;

public class OwnerMenu extends TextMenu {

    // Instance variable for owner service
    private final OwnerService ownerService;

    private boolean returnToMainMenu = false;

    // Constructor
    public OwnerMenu(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Owner Management Menu:");
        printOption("1", "Create a new owner");
        printOption("2", "View all owners");
        printOption("3", "Update owner details");
        printOption("4", "Delete an owner");
        printOption("5", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {

        int choice = InputUtils.getValidatedInput("Enter your choice: ", 1, 5);

        switch (choice) {
            case 1:
                createNewOwner();
                break;
            case 2:
                viewAllOwners();
                break;
            case 3:
                // updateOwnerDetails();
                break;
            case 4:
                // deleteOwner();
                break;
            case 5:
                System.out.println("Returning to main menu...");
                returnToMainMenu = true; // Set flag to true
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    private void createNewOwner() {
        String ownerName = InputUtils.getStringInput("Enter the name of the new owner: ");
        Owner owner = new Owner(ownerName);
        ownerService.saveOwner(owner);
        System.out.println("Owner '" + ownerName + "' created successfully!");
    }

    // Method to view all owners
    private void viewAllOwners() {
        List<Owner> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found.");
        } else {
            System.out.println("List of all owners:");
            for (Owner owner : owners) {
                System.out.println(owner.getOwner_name());
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
