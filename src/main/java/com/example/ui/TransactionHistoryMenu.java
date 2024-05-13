package com.example.ui;

import com.example.model.BankAccount;
import com.example.ui.BankAccountMenu;
import com.example.model.Transaction;
import com.example.service.TransactionService;
import com.example.util.InputUtils;

import java.util.List;

public class TransactionHistoryMenu extends TextMenu {

    private final TransactionService transactionService;
    private boolean returnToMainMenu = false;

    public TransactionHistoryMenu(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Transaction History Menu:");
        printOption("1", "Select a bank account");
        printOption("2", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {

        int choice = InputUtils.getValidatedInput(MenuConstants.ENTER_CHOICE_PROMPT, 1, 2);

        switch (choice) {
            case 1:
                selectBankAccount();
                break;
            case 2:
                System.out.println(MenuConstants.RETURNING_TO_MAIN_MENU);
                returnToMainMenu = true;
                break;
            default:
                System.out.println(MenuConstants.INVALID_CHOICE_PROMPT);
                break;
        }
    }

    private void selectBankAccount() {
        List<BankAccount> bankAccounts = transactionService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        System.out.println("Select a bank account to review its transaction history:");
/*        for (int i = 0; i < bankAccounts.size(); i++) {
            System.out.println((i + 1) + ". " + bankAccounts.get(i).getAccountId());
        }*/

        // Display the formatted list of bank accounts
        BankAccountUtils.displayFormattedBankAccounts(bankAccounts);

        int accountChoice = InputUtils.getValidatedInput("Enter your choice: ", 1, bankAccounts.size());
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1); // Adjust for 0-based indexing

        // Now you can display the transaction history of the chosen account
        List<Transaction> transactions = transactionService.getTransactionHistory(chosenAccount);
        displayTransactionHistory(transactions);
    }

    private void displayTransactionHistory(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            System.out.println("No transactions found for this account.");
            return;
        }

        System.out.println("Transaction History:");
        for (Transaction transaction : transactions) {
            System.out.println("Transaction ID: " + transaction.getTransaction_id());
            System.out.println("Amount: " + transaction.getAmount());
            System.out.println("Date: " + transaction.getTransaction_date());
            System.out.println("--------------------");
        }
    }


    public void run() {
        while (!returnToMainMenu) {
            displayMenu();
            handleUserInput();
        }
        returnToMainMenu = false;
    }
}
