package com.example.ui;

import com.example.model.Bank;
import com.example.model.BankAccount;
import com.example.model.Owner;
import com.example.service.BankAccountService;
import com.example.service.OwnerService;
import com.example.service.BankService;
import com.example.util.InputUtils;
import com.example.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;

public class BankAccountMenu extends TextMenu {
    private static final String ACCOUNT_HEADER = "Account";
    private static final String BALANCE_HEADER = "Balance";
    private static final String BANK_HEADER = "Bank";
    private static final String OWNERS_HEADER = "Owners";
    private static final String INDEX_HEADER = "Index";


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

        int choice = InputUtils.getValidatedInput("Enter your choice: ", 1, 5);

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
    }

    private void depositFunds() {

        List<BankAccount> bankAccounts = bankAccountService.getAllBankAccounts();
        if (bankAccounts.isEmpty()) {
            System.out.println("No bank accounts found. Please create a bank account first.");
            return;
        }

        displayFormattedBankAccounts(bankAccounts);

        int accountChoice = InputUtils.getValidatedInput("Enter your choice: ", 1, bankAccounts.size());
        BankAccount chosenAccount = bankAccounts.get(accountChoice - 1); // Adjust for 0-based indexing

        BigDecimal amount = InputUtils.getBigDecimalInput("Enter the amount to deposit: ");
        chosenAccount.setBalance(chosenAccount.getBalance().add(amount));

        bankAccountService.updateBankAccount(chosenAccount);
        System.out.println("Funds deposited successfully. New balance: " + chosenAccount.getBalance());
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
            System.out.println("No bank accounts found.");
        } else {
            int longestAccountNameLength = Math.max(StringUtils.findLongestStringLength(bankAccounts, BankAccount::getAccount_name), ACCOUNT_HEADER.length());
            int longestBankNameLength = Math.max(StringUtils.findLongestStringLength(bankAccounts, bankAccount -> bankAccount.getBank().getBank_name()), BANK_HEADER.length());
            int longestBalanceLength = Math.max(StringUtils.findLongestStringLength(bankAccounts, bankAccount -> bankAccount.getBalance().toString()), BALANCE_HEADER.length());
   /*         int longestOwnersNamesLength = Math.max(StringUtils.findLongestStringLength(bankAccounts, account -> {
                Set<Owner> owners = account.getOwners();
                StringBuilder ownersNames = new StringBuilder();
                for (Owner owner : owners) {
                    if (ownersNames.length() > 0) {
                        ownersNames.append(", "); // Add comma and space if not the first owner
                    }
                    ownersNames.append(owner.getOwner_name());
                }
                return ownersNames.toString();
            }), OWNERS_HEADER.length());*/
            int longestOwnersNamesLength = Math.max(StringUtils.findLongestStringLength(bankAccounts, account -> {
                Set<Owner> owners = account.getOwners();
                StringJoiner ownersNames = new StringJoiner(", ");
                for (Owner owner : owners) {
                    ownersNames.add(owner.getOwner_name());
                }
                return ownersNames.toString();
            }), OWNERS_HEADER.length());

            int totalLength = INDEX_HEADER.length() + longestAccountNameLength + longestBalanceLength + longestBankNameLength + longestOwnersNamesLength
                    + " | ".length() * 4;
            String separator = "-".repeat(totalLength);

            System.out.println("longestAccountNameLength: " + longestAccountNameLength);
            System.out.println("longestBalanceLength: " + longestBalanceLength);
            System.out.println("longestBankNameLength: " + longestBankNameLength);
            System.out.println("longestOwnersNamesLength: " + longestOwnersNamesLength);
            System.out.println("totalLength: " + totalLength);

            for (int i = 0; i < bankAccounts.size(); i++) {
                System.out.println(formatBankAccountAndHeader(separator, i + 1, bankAccounts.get(i), longestAccountNameLength, longestBalanceLength,
                        longestBankNameLength, longestOwnersNamesLength));
            }
        }
    }

    private String formatBankAccountAndHeader(String separator, int index, BankAccount bankAccount, int longestAccountNameLength,
                                              int longestBalanceLength, int longestBankNameLength, int longestOwnerNameLength) {
        String accountColumnWidthSpecifier = "%-" + (longestAccountNameLength) + "s";
        String bankColumnWidthSpecifier = "%-" + (longestBankNameLength) + "s";
        String balanceColumnWidthSpecifier = "%-" + (longestBalanceLength) + ".2f";
        String balanceHeaderColumnWidthSpecifier = "%-" + (longestBalanceLength) + "s";
        String ownersColumnWidthSpecifier = "%-" + (longestOwnerNameLength) + "s\n";


        if (index == 1) {
            return String.format("\nList of all bank accounts:\n" +
                            separator + "\n" +
                            "Index | " + accountColumnWidthSpecifier + " | " + balanceHeaderColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | " + ownersColumnWidthSpecifier +
                            separator + "\n" +
                            "%-5d | " + accountColumnWidthSpecifier + " | " + balanceColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | " + ownersColumnWidthSpecifier,
                    ACCOUNT_HEADER, BALANCE_HEADER, BANK_HEADER, OWNERS_HEADER,
                    index,
                    bankAccount.getAccount_name(),
                    bankAccount.getBalance(),
                    bankAccount.getBank().getBank_name(),
                    getOwnersAsString(bankAccount.getOwners())) +
                    separator;
        } else {
            return String.format("%-5d | " + accountColumnWidthSpecifier + " | " + balanceColumnWidthSpecifier + " | " + bankColumnWidthSpecifier + " | " + ownersColumnWidthSpecifier,
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
