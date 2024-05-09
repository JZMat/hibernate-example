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

        viewAllBankAccounts();

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
            int longestAccountNameLength = findLongestBankAccountNameLength(bankAccounts);
            int longestBankNameLength = findLongestBankNameLength(bankAccounts);
            int longestBalanceLength = findLongestBalanceLength(bankAccounts);
            int totalLength = "Index".length() + longestAccountNameLength + longestBalanceLength + longestBankNameLength + "Zorro, Jasio".length()
                    + " | ".length() * 4;

            System.out.println("\nseparator length: " + totalLength + ":\n");
            System.out.println("longestAccountNameLength: " + longestAccountNameLength);
            System.out.println("longestBalanceLength: " + longestBalanceLength);
            System.out.println("longestBankNameLength: " + longestBankNameLength);
            System.out.println("longestOwnersLength: " + "Zorro, Jasio".length());

            for (int i = 0; i < bankAccounts.size(); i++) {
                System.out.println(formatBankAccount(totalLength, i + 1, bankAccounts.get(i), longestAccountNameLength, longestBalanceLength, longestBankNameLength));
            }
        }
    }

    private int findLongestBalanceLength(List<BankAccount> bankAccounts) {
        int maxLength = 0;
        for (BankAccount account : bankAccounts) {
            String balanceString = account.getBalance().toString();
            int length = balanceString.length();
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    private int findLongestBankNameLength(List<BankAccount> bankAccounts) {
        int maxLength = 0;
        for (BankAccount account : bankAccounts) {
            int length = account.getBank().getBank_name().length();
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    public int findLongestBankAccountNameLength(List<BankAccount> bankAccounts) {
        int maxLength = 0;
        for (BankAccount account : bankAccounts) {
            int length = account.getAccount_name().length();
            if (length > maxLength) {
                maxLength = length;
            }
        }
        return maxLength;
    }

    private String formatBankAccount(int totalLength, int index, BankAccount bankAccount, int longestAccountNameLength,
                                     int longestBalanceLength, int longestBankNameLength) {
        String accountColumnWidthSpecifier = "%-" + (longestAccountNameLength) + "s";
        String bankColumnWidthSpecifier = "%-" + (longestBankNameLength) + "s";
        String balanceColumnWidthSpecifier = "%-" + (longestBalanceLength) + ".2f";
        String balanceHeaderColumnWidthSpecifier = "%-" + (longestBalanceLength) + "s";
        String separator = "-".repeat(totalLength);

        if (index == 1) {
            return String.format("\nList of all bank accounts:\n" +
                            separator + "\n" +
                            "Index | " + accountColumnWidthSpecifier + " | " + balanceHeaderColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | %-40s\n" +
                            separator + "\n" +
                            "%-5d | " + accountColumnWidthSpecifier + " | " + balanceColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | %s\n",
                    "Account", "Balance", "Bank", "Owners",
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        } else {
            return String.format("%-5d | " + accountColumnWidthSpecifier + " | " + balanceColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | %s\n",
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        }
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

        viewAllBankAccounts();

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
