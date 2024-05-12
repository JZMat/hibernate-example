package com.example.ui;

import com.example.model.Bank;
import com.example.model.BankAccount;
import com.example.model.Owner;
import com.example.service.BankAccountService;
import com.example.service.OwnerService;
import com.example.service.BankService;
import com.example.util.InputUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
        printOption("4", "Disconnect an owner from a bank account");
        printOption("5", "Deposit funds into an account");
        printOption("6", "Transfer money between accounts");
        printOption("7", "Go back to main menu");
    }

    @Override
    public void handleUserInput() {

        int choice = InputUtils.getValidatedInput(MenuConstants.ENTER_CHOICE_PROMPT, 1, 6);

        switch (choice) {
            case 1:
                createNewBankAccount();
                break;
            case 2:
                displayFormattedBankAccounts(retrieveAllBankAccounts());
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

        displayFormattedBankAccounts(bankAccounts);

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

        System.out.println("Transfer successful!");
    }


    private void disconnectOwnerFromBankAccount() {
        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        displayFormattedBankAccounts(bankAccounts);

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

/*    private void disconnectOwnerFromBankAccount() {
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

        displayFormattedBankAccounts(bankAccounts);

        int accountChoice = InputUtils.getValidatedInput("Choose a bank account by entering its number: ", 1, bankAccounts.size());
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1);

        // Check if the owner is associated with the bank account
        if (!chosenAccount.getOwners().contains(chosenOwner)) {
            System.out.println("The chosen owner is not associated with the chosen bank account.");
            return;
        }

        // Remove the owner from the bank account
        chosenAccount.getOwners().remove(chosenOwner);
        bankAccountService.updateBankAccount(chosenAccount);

        System.out.println("Owner '" + chosenOwner.getOwner_name() + "' has been disconnected from bank account '" + chosenAccount.getAccount_name() + "'.");
    }*/


    private void depositFunds() {

        try {
            List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
            if (bankAccounts.isEmpty()) {
                System.out.println("No bank accounts found. Please create a bank account first.");
                return;
            }

            displayFormattedBankAccounts(bankAccounts);

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

    private void displayFormattedBankAccounts(List<BankAccount> bankAccounts) {

        if (bankAccounts.isEmpty()) {
            System.out.println(MenuConstants.NO_BANK_ACCOUNTS_FOUND);
        } else {

            List<String> accountNames = bankAccounts.stream()
                    .map(BankAccount::getAccount_name)
                    .collect(Collectors.toList());
            int maxAcctNameLen = Math.max(findLongestStringLength(accountNames), MenuConstants.ACCOUNT_HEADER.length());

            List<String> bankNames = bankAccounts.stream()
                    .map(bankAccount -> bankAccount.getBank().getBank_name())
                    .collect(Collectors.toList());
            int maxBankNameLen = Math.max(findLongestStringLength(bankNames), MenuConstants.BANK_HEADER.length());

            List<String> balances = bankAccounts.stream()
                    .map(bankAccount -> bankAccount.getBalance().toString())
                    .collect(Collectors.toList());
            int maxBalanceLen = Math.max(findLongestStringLength(balances), MenuConstants.BALANCE_HEADER.length());

            List<String> ownersNames = bankAccounts.stream()
                    .map(account -> {
                        Set<Owner> owners = account.getOwners();
                        StringJoiner ownersNamesJoiner = new StringJoiner(", ");
                        for (Owner owner : owners) {
                            ownersNamesJoiner.add(owner.getOwner_name());
                        }
                        return ownersNamesJoiner.toString();
                    })
                    .collect(Collectors.toList());

            int maxOwnersNamesLen = Math.max(findLongestStringLength(ownersNames), MenuConstants.OWNERS_HEADER.length());

            int totalLen = MenuConstants.INDEX_HEADER.length() + maxAcctNameLen + maxBalanceLen + maxBankNameLen + maxOwnersNamesLen
                    + " | ".length() * 4;
            String separator = "-".repeat(totalLen);

            System.out.println("maxAcctNameLen: " + maxAcctNameLen);
            System.out.println("maxBalanceLen: " + maxBalanceLen);
            System.out.println("maxBankNameLen: " + maxBankNameLen);
            System.out.println("maxOwnersNamesLen: " + maxOwnersNamesLen);
            System.out.println("totalLen: " + totalLen);

            for (int i = 0; i < bankAccounts.size(); i++) {
                System.out.println(formatBankAccountAndHeader(separator, i + 1, bankAccounts.get(i), maxAcctNameLen, maxBalanceLen,
                        maxBankNameLen, maxOwnersNamesLen));
            }
        }
    }

    private int findLongestStringLength(List<String> strings) {
        return strings.stream()
                .mapToInt(String::length)
                .max()
                .orElse(0);
    }

    private String formatBankAccountAndHeader(String separator, int index, BankAccount bankAccount, int maxAccountNameLen,
                                              int maxBalanceLen, int maxBankNameLen, int maxOwnerNameLen) {
        String accountColWidthSpecifier = "%-" + (maxAccountNameLen) + "s";
        String bankColWidthSpecifier = "%-" + (maxBankNameLen) + "s";
        String balanceColWidthSpecifier = "%-" + (maxBalanceLen) + ".2f";
        String balanceHeaderColWidthSpecifier = "%-" + (maxBalanceLen) + "s";
        String ownersColWidthSpecifier = "%-" + (maxOwnerNameLen) + "s\n";


        if (index == 1) {
            return String.format("\nList of all bank accounts:\n" +
                            separator + "\n" +
                            "Index | " + accountColWidthSpecifier + " | " + balanceHeaderColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier +
                            separator + "\n" +
                            "%-5d | " + accountColWidthSpecifier + " | " + balanceColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier,
                    MenuConstants.ACCOUNT_HEADER, MenuConstants.BALANCE_HEADER, MenuConstants.BANK_HEADER, MenuConstants.OWNERS_HEADER,
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        } else {
            return String.format("%-5d | " + accountColWidthSpecifier + " | " + balanceColWidthSpecifier + " | " + bankColWidthSpecifier + " | " + ownersColWidthSpecifier,
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

        displayFormattedBankAccounts(bankAccounts);

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
