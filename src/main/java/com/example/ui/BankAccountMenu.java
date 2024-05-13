package com.example.ui;

import com.example.model.Bank;
import com.example.model.BankAccount;
import com.example.model.Owner;
import com.example.service.BankAccountService;
import com.example.service.OwnerService;
import com.example.service.BankService;
import com.example.service.TransactionService;
import com.example.util.InputUtils;
import com.example.model.Transaction;

import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.*;

public class BankAccountMenu extends TextMenu {

    private final BankAccountService bankAccountService;
    private final OwnerService ownerService;
    private final BankService bankService;
    private boolean returnToMainMenu = false;
    private final TransactionService transactionService;

    public BankAccountMenu(BankAccountService bankAccountService, OwnerService ownerService, BankService bankService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.ownerService = ownerService;
        this.bankService = bankService;
        this.transactionService = transactionService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Bank Account Management Menu:");
        printOption("1", "Create a new bank account");
        printOption("2", "View all bank accounts");
        printOption("3", "Assign an owner to a bank account");
        printOption("4", "Disconnect an owner from a bank account");
        printOption("5", "Deposit funds into an account");
        printOption("6", "Transfer money between accounts");
        printOption("7", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {

        int choice = InputUtils.getValidatedInput(MenuConstants.ENTER_CHOICE_PROMPT, 1, 7);

        switch (choice) {
            case 1:
                createNewBankAccount();
                break;
            case 2:
                BankAccountUtils.displayFormattedBankAccounts(retrieveAllBankAccounts());
                break;
            case 3:
                assignOwnerToBankAccount();
                break;

            case 4:
                disconnectOwnerFromBankAccount();
                break;

            case 5:
                depositFunds();
                break;
            case 6:
                transferMoneyBetweenAccounts();
                break;
            case 7:
                System.out.println(MenuConstants.RETURNING_TO_MAIN_MENU);
                returnToMainMenu = true;
                break;
            default:
                System.out.println(MenuConstants.INVALID_CHOICE_PROMPT);
                break;
        }
    }

    private void transferMoneyBetweenAccounts() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        BankAccountUtils.displayFormattedBankAccounts(bankAccounts);

        int sourceAccountChoice = InputUtils.getValidatedInput("Choose the source account by entering its number: ", 1, bankAccounts.size());
        BankAccount sourceAccount = bankAccounts.get(sourceAccountChoice - 1); // Adjust for 0-based indexing

        int destinationAccountChoice = InputUtils.getValidatedInput("Choose the destination account by entering its number: ", 1, bankAccounts.size());
        BankAccount destinationAccount = bankAccounts.get(destinationAccountChoice - 1); // Adjust for 0-based indexing

        BigDecimal amount = InputUtils.getBigDecimalInput("Enter the amount to transfer: ");

        if (sourceAccount.getBalance().compareTo(amount) < 0) {
            System.out.println("Insufficient balance in source account.");
            return;
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        destinationAccount.setBalance(destinationAccount.getBalance().add(amount));

        bankAccountService.updateBankAccount(sourceAccount);
        bankAccountService.updateBankAccount(destinationAccount);

        // Create a new Transaction object
        Transaction transaction = new Transaction();
        transaction.setSourceAccount(sourceAccount);
        transaction.setDestinationAccount(destinationAccount);
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now()); // Set the transaction date to the current date and time

        // Save the Transaction object to the database
        transactionService.saveTransaction(transaction);

        System.out.println("Transfer successful!");
    }


    private void disconnectOwnerFromBankAccount() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        BankAccountUtils.displayFormattedBankAccounts(bankAccounts);

        int accountChoice = InputUtils.getValidatedInput("Choose a bank account by entering its number: ", 1, bankAccounts.size());
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1);

        Set<Owner> owners = chosenAccount.getOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found for this account.");
            return;
        }

        // Print list of owners
        System.out.println("List of all owners for this account:");
        List<Owner> ownersList = new ArrayList<>(owners);
        for (int i = 0; i < ownersList.size(); i++) {
            System.out.println((i + 1) + ". " + ownersList.get(i).getOwner_name());
        }

        int ownerChoice = InputUtils.getValidatedInput("Choose an owner by entering his number: ", 1, ownersList.size());
        Owner chosenOwner = ownersList.get(ownerChoice - 1);

        // Remove the owner from the bank account
        chosenAccount.removeOwner(chosenOwner);
        bankAccountService.updateBankAccount(chosenAccount);

        System.out.println("Owner '" + chosenOwner.getOwner_name() + "' has been disconnected from bank account '" + chosenAccount.getAccount_name() + "'.");
    }

    private void depositFunds() {

        try {
            List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
            if (bankAccounts.isEmpty()) {
                System.out.println("No bank accounts found. Please create a bank account first.");
                return;
            }

            BankAccountUtils.displayFormattedBankAccounts(bankAccounts);

            int accountChoice = InputUtils.getValidatedInput("Choose a bank by entering its number: ", 1, bankAccounts.size());
            BankAccount chosenAccount = bankAccounts.get(accountChoice - 1); // Adjust for 0-based indexing

            BigDecimal amount = InputUtils.getBigDecimalInput("Enter the amount to deposit: ");
            chosenAccount.setBalance(chosenAccount.getBalance().add(amount));

            bankAccountService.updateBankAccount(chosenAccount);
            System.out.println("Funds deposited successfully. New balance: " + chosenAccount.getBalance());
        } catch (Exception e) {
            // throw new RuntimeException(e);
            System.out.println("An error occurred while trying to deposit funds: " + e.getMessage());
        }
    }

    private List<BankAccount> retrieveAllBankAccounts() {
        return bankAccountService.getAllBankAccounts();
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

        int bankChoice = InputUtils.getValidatedInput("Choose a bank by entering its number: ", 1, banks.size());
        Bank chosenBank = banks.get(bankChoice - 1); // Adjust for 0-based indexing

        BankAccount bankAccount = new BankAccount(accountName, BigDecimal.ZERO, chosenBank);
        bankAccountService.saveBankAccount(bankAccount);
        System.out.println("Bank account '" + accountName + "' created successfully in '" + chosenBank.getBank_name() + "' bank!");
    }


    private void assignOwnerToBankAccount() {

        List<Owner> owners = ownerService.getAllOwners();
        if (owners.isEmpty()) {
            System.out.println("No owners found. Please create an owner first.");
            return;
        }

        // Print list of owners
        System.out.println("List of all owners:");
        for (int i = 0; i < owners.size(); i++) {
            System.out.println((i + 1) + ". " + owners.get(i).getOwner_name());
        }

        int ownerChoice = InputUtils.getValidatedInput("Choose an owner by entering his number: ", 1, owners.size());
        Owner chosenOwner = owners.get(ownerChoice - 1);

        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        BankAccountUtils.displayFormattedBankAccounts(bankAccounts);

        // int accountChoice = InputUtils.getIntInput("Choose a bank account by entering their number: ");
        int accountChoice = InputUtils.getValidatedInput("Choose a bank account by entering its number: ", 1, bankAccounts.size());
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
