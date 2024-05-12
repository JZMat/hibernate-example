package com.example.ui;

import com.example.service.BankAccountService;
import com.example.util.InputUtils;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Stream;

public class BalanceSheetMenu extends TextMenu {
    private final BankAccountService bankAccountService;
    private boolean returnToMainMenu = false;
    private BigDecimal totalBalance;

    public BalanceSheetMenu(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @Override
    public void displayMenu() {
        System.out.println("Balance Sheet Menu:");
        printOption("1", "View total balance by owner");
        printOption("2", "View balance for each owner by bank and account");
        printOption("3", "View total balance");
        printOption("4", "View balance for each owner by bank");
        printOption("5", "Go back to main menu");
        printOption("6", "Create PDF");
    }

    @Override
    public void handleUserInput() {
        int choice = InputUtils.getValidatedInput("Enter your choice: ", 1, 6);
        switch (choice) {
            case 1:
                // Code to display total balance of all accounts by owner
                Map<String, BigDecimal> totalBalanceForEachOwner = bankAccountService.getTotalBalanceForEachOwner();
                totalBalance = BigDecimal.ZERO;
                System.out.println("Total balance of all accounts by owner: ");
                for (Map.Entry<String, BigDecimal> entry : totalBalanceForEachOwner.entrySet()) {
                    String ownerName = entry.getKey();
                    BigDecimal balance = entry.getValue();
                    totalBalance = totalBalance.add(balance);
                    // System.out.println("    " + ownerName + ": " + balance);
                    System.out.printf("    %-15s %10.2f%n", ownerName, balance);
                }
                System.out.println("--------------------------------");
                // System.out.println("Total: " + totalBalance);
                System.out.printf("    %-15s %10.2f%n", "Total", totalBalance);
                break;

            case 2:

                // Code to display balance for each owner by bank and account
                totalBalance = BigDecimal.ZERO;
                Map<String, Map<String, BigDecimal>> balanceForEachOwner = bankAccountService.getBalanceForEachOwnerByBankAndAccount();
                for (Map.Entry<String, Map<String, BigDecimal>> entry : balanceForEachOwner.entrySet()) {
                    String ownerNames = entry.getKey();
                    Map<String, BigDecimal> balances = entry.getValue();
                    if (ownerNames.contains(",")) {
                        System.out.println("Owners: " + ownerNames);
                    } else {
                        System.out.println("Owner: " + ownerNames);
                    }
                    for (Map.Entry<String, BigDecimal> balanceEntry : balances.entrySet()) {
                        String accountInfo = balanceEntry.getKey();
                        BigDecimal balance = balanceEntry.getValue();
                        // Add the balance of the current account to the total balance
                        totalBalance = totalBalance.add(balance);
                        // System.out.println("    " + accountInfo + ": " + balance);
                        System.out.printf("    %-45s %10.2f%n", accountInfo, balance);
                    }
                    // Print the total balance
                    // System.out.println("--------------------------------------------old");
                    String separator = "-".repeat(60);
                    System.out.println(separator);
                    System.out.printf("    %-45s %10.2f%n", "Wrong Total balance", totalBalance);
                }
                break;
            case 3:
                // Code to display total balance
                totalBalance = bankAccountService.getTotalBalanceOfAllAccountsByOwner();
                System.out.println("Total balance : " + totalBalance);
                break;
            case 4:
                // Code to display total balance by owner and bank
                Map<String, Map<String, BigDecimal>> totalBalanceForEachOwnerByBank = bankAccountService.getTotalBalanceForEachOwnerByBank();
                totalBalance = BigDecimal.ZERO;
                for (Map.Entry<String, Map<String, BigDecimal>> entry : totalBalanceForEachOwnerByBank.entrySet()) {
                    String ownerNames = entry.getKey();
                    Map<String, BigDecimal> balances = entry.getValue();
                    if (ownerNames.contains(",")) {
                        System.out.println("Owners: " + ownerNames);
                    } else {
                        System.out.println("Owner: " + ownerNames);
                    }
                    for (Map.Entry<String, BigDecimal> balanceEntry : balances.entrySet()) {
                        String bankName = balanceEntry.getKey();
                        BigDecimal balance = balanceEntry.getValue();
                        totalBalance = totalBalance.add(balance);
                        // System.out.println("    " + bankName + ": " + balance);
                        System.out.printf("    %-15s %10.2f%n", bankName, balance);
                    }
                }
                String separator = "-".repeat(30);
                System.out.println(separator);
                System.out.printf("    %-15s %10.2f%n", "Total balance", totalBalance);
                break;

            case 5:
                System.out.println(MenuConstants.RETURNING_TO_MAIN_MENU);
                returnToMainMenu = true;
                return;


            case 6:
                generatePdfReport();
                break;


            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    public void generatePdfReport() {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("BalanceSheetReport.pdf"));
            document.open();

            PdfPTable table = new PdfPTable(4); // 4 columns: Owner, Bank, Account, Balance
            //PdfPCell cell;

            // Add headers
            Stream.of("Owner", "Bank", "Account", "Balance")
                    .forEach(header -> {
                        PdfPCell cell = new PdfPCell(new Phrase(header));
                        table.addCell(cell);
                    });



            Map<String, Map<String, BigDecimal>> totalBalanceForEachOwnerByBank = bankAccountService.getTotalBalanceForEachOwnerByBank();
            BigDecimal totalBalance = BigDecimal.ZERO;

            for (Map.Entry<String, Map<String, BigDecimal>> entry : totalBalanceForEachOwnerByBank.entrySet()) {
                String ownerNames = entry.getKey();
                Map<String, BigDecimal> balances = entry.getValue();

                for (Map.Entry<String, BigDecimal> balanceEntry : balances.entrySet()) {
                    String accountInfo = balanceEntry.getKey();
                    BigDecimal balance = balanceEntry.getValue();
                    totalBalance = totalBalance.add(balance);

                    // Split the accountInfo into bankName and accountName
                    String[] parts = accountInfo.split(" - ", 2);
                    String bankName = parts[0];
                    String accountName = parts.length > 1 ? parts[1] : "";

                    // Add cells for the ownerNames, bankName, accountName, and balance
                    table.addCell(new Phrase(ownerNames));
                    table.addCell(new Phrase(bankName));
                    table.addCell(new Phrase(accountName));
                    table.addCell(new Phrase(balance.toString()));
                }
            }



            // Add empty cells for the "Owner" and "Bank" columns
            table.addCell("");
            table.addCell("");
            table.addCell("Total");
            table.addCell(new Phrase(totalBalance.toString()));
            // Add total balance
  /*          Paragraph totalBalanceParagraph = new Paragraph("Total balance: " + totalBalance);
            document.add(totalBalanceParagraph);*/
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
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

