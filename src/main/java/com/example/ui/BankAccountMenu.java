package com.example.ui;

import com.example.model.Bank;
import com.example.model.BankAccount;
import com.example.model.Owner;
import com.example.service.BankAccountService;
import com.example.service.OwnerService;
import com.example.service.BankService;
import com.example.util.InputUtils;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Set;

public class BankAccountMenu extends TextMenu {
    private final BankAccountService bankAccountService;
    private final OwnerService ownerService;
    private final BankService bankService;
    private boolean returnToMainMenu = false;

    public BankAccountMenu(BankAccountService bankAccountService, OwnerService ownerService, BankService bankService) {
        this.bankAccountService = bankAccountService;
        this.ownerService = ownerService;
        this.bankService = bankService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Bank Account Management Menu:");
        printOption("1", "Create a new bank account");
        printOption("2", "View all bank accounts");
        printOption("3", "Assign an owner to a bank account");
        printOption("4", "Deposit funds into an account");
        printOption("5", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {
        try {
            int choice = InputUtils.getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    createNewBankAccount();
                    break;
                case 2:
                    viewAllBankAccounts();
                    break;
                case 3:
                    assignOwnerToBankAccount();
                    break;
                case 4:
                    depositFunds();
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    returnToMainMenu = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid input type. Please enter a number.");
        }
    }

    private void depositFunds() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        System.out.println("List of all bank accounts:");
  /*      for (int i = 0; i < bankAccounts.size(); i++) {
            BankAccount bankAccount = bankAccounts.get(i);
            System.out.println((i + 1) + ". " + formatBankAccount(bankAccounts.get(i))); //+ bankAccount.getAccount_name() + " - Balance: " + bankAccount.getBalance());
        }*/for (int i = 0; i < bankAccounts.size(); i++) {
            System.out.println(formatBankAccount(i + 1, bankAccounts.get(i)));
        }


        //int accountChoice = InputUtils.getIntInput("Choose a bank account by entering its number: ");

        int accountChoice;
        while (true) {
            try {
                accountChoice = InputUtils.getIntInput("Choose a bank account by entering its number: ");
                if (accountChoice < 1 || accountChoice > bankAccounts.size()) {
                    throw new IndexOutOfBoundsException();
                }
                break; // Break the loop if input is valid
            } catch (InputMismatchException e) {
                System.out.println("Invalid input type. Please enter a number.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid account number. Please choose a number within the provided range.");
            }
        }
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1); // Adjust for 0-based indexing

        BigDecimal amount = InputUtils.getBigDecimalInput("Enter the amount to deposit: ");
        chosenAccount.setBalance(chosenAccount.getBalance().add(amount));

        bankAccountService.updateBankAccount(chosenAccount);
        System.out.println("Funds deposited successfully. New balance: " + chosenAccount.getBalance());
    }

    private void createNewBankAccount() {
        String accountName = InputUtils.getStringInput("Enter the name of the new bank account: ");

        List<Bank> banks = bankService.getAllBanks();
        if (banks.isEmpty()) {
            System.out.println("No banks found. Please create a bank first.");
            return;
        }

        System.out.println("List of all banks:");
        for (int i = 0; i < banks.size(); i++) {
            System.out.println((i + 1) + ". " + banks.get(i).getBank_name());
        }

        int bankChoice = InputUtils.getIntInput("Choose a bank by entering their number: ");
        Bank chosenBank = banks.get(bankChoice - 1);

        BankAccount bankAccount = new BankAccount(accountName, BigDecimal.ZERO, chosenBank);
        bankAccountService.saveBankAccount(bankAccount);
        System.out.println("Bank account '" + accountName + "' created successfully in '" + chosenBank.getBank_name() + "' bank!");
    }

    private void viewAllBankAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found.");
        } else {
            System.out.println("List of all bank accounts:");
         /*   for (BankAccount bankAccount : bankAccounts) {
                System.out.println(formatBankAccount(bankAccount));
                //System.out.println(displayBankAccounts(bankAccounts)););
            }*/

            for (int i = 0; i < bankAccounts.size(); i++) {
                System.out.println(formatBankAccount(i + 1, bankAccounts.get(i)));
            }
      /*      for (BankAccount bankAccount : bankAccounts) {
                System.out.println("Account Name: " + bankAccount.getAccount_name() +
                        " - Balance: " + bankAccount.getBalance() +
                        " - Bank: " + bankAccount.getBank().getBank_name() +
                        " - Owners: " + getOwnersAsString(bankAccount.getOwners()));
            }*/
        }
    }

 /*   private void displayBankAccounts(List<BankAccount> bankAccounts) {
        for (int i = 0; i < bankAccounts.size(); i++) {
            BankAccount bankAccount = bankAccounts.get(i);
            System.out.println(formatBankAccount(getIndex(i), bankAccount));
        }
    }

    private int getIndex(int currentIndex) {
        return currentIndex + 1; // Adjusting the index to start from 1
    }*/

    private String formatBankAccount(int index, BankAccount bankAccount) {
        return String.format("%3d | %-40s | %-10s | %-20s | %s",
                index,
                bankAccount.getAccount_name(),
                bankAccount.getBalance(),
                bankAccount.getBank().getBank_name(),
                getOwnersAsString(bankAccount.getOwners())
        );
    }


    private String oldformatBankAccount(BankAccount bankAccount) {
        StringBuilder stringBuilder = new StringBuilder();

        String header = String.format("%-40s | %-10s | %-20s | %s\n",
                "Account Name", "Balance", "Bank", "Owners");

        stringBuilder.append(header);
        stringBuilder.append("-".repeat(93)).append("\n"); // Add horizontal line

        String accountName = bankAccount.getAccount_name();
        BigDecimal balance = bankAccount.getBalance();
        String bankName = bankAccount.getBank().getBank_name();
        String owners = getOwnersAsString(bankAccount.getOwners());

        String formattedString = String.format("%-40s | %-10.2f | %-20s | %s\n",
                accountName, balance, bankName, owners);

        stringBuilder.append(formattedString);

        return stringBuilder.toString();
    }


    private String getOwnersAsString(Set<Owner> owners) {
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


    private void assignOwnerToBankAccount() {
        List<Owner> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found. Please create an owner first.");
            return;
        }

        System.out.println("List of all owners:");
        for (int i = 0; i < owners.size(); i++) {
            System.out.println((i + 1) + ". " + owners.get(i).getOwner_name());
        }

        int ownerChoice = InputUtils.getIntInput("Choose an owner by entering their number: ");
        Owner chosenOwner = owners.get(ownerChoice - 1);

        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        System.out.println("List of all bank accounts:");
     /*   for (int i = 0; i < bankAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + formatBankAccount(bankAccounts.get(i))); // bankAccounts.get(i).getAccount_name());
        }*/
        for (int i = 0; i < bankAccounts.size(); i++) {
            System.out.println(formatBankAccount(i + 1, bankAccounts.get(i)));
        }

        int accountChoice = InputUtils.getIntInput("Choose a bank account by entering their number: ");
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1);

        // Access the owners set to initialize it while the session is still open
        int ownerSetSize = chosenAccount.getOwners().size();

        chosenAccount.addOwner(chosenOwner);
        bankAccountService.updateBankAccount(chosenAccount);

        System.out.println("Owner '" + chosenOwner.getOwner_name() + "' has been assigned to bank account '" + chosenAccount.getAccount_name() + "'.");
    }

    public void run() {
        while (!returnToMainMenu) {
            displayMenu();
            handleUserInput();
        }
        returnToMainMenu = false;
    }
}
