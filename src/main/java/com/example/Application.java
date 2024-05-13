package com.example;

import com.example.repository.*;
import com.example.service.*;
import com.example.ui.MainMenu;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Application {
    public static void main(String[] args) {
        System.out.println("Welcome to the Banking System!");

        // Create Hibernate SessionFactory
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Create BankRepository instance
        BankRepository bankRepository = new BankRepositoryImpl(sessionFactory);
        // Create BankService instance
        BankService bankService = new BankServiceImpl(bankRepository);

        // Create OwnerRepository instance
        OwnerRepository ownerRepository = new OwnerRepositoryImpl(sessionFactory);
        // Create OwnerService instance
        OwnerService ownerService = new OwnerServiceImpl(ownerRepository);

        // Create BankAccountRepository instance
        BankAccountRepository bankAccountRepository = new BankAccountRepositoryImpl(sessionFactory);
        //Create BankAccountService instance
        BankAccountService bankAccountService = new BankAccountServiceImpl(bankAccountRepository);

        // Create TransactionRepository instance
        TransactionRepository transactionRepository = new TransactionRepositoryImpl(sessionFactory);
        // Create TransactionService instance
        TransactionService transactionService = new TransactionServiceImpl(transactionRepository, bankAccountRepository);

        // Create and run the main menu with the BankService instance, OwnerService instance ...
        MainMenu mainMenu = new MainMenu(bankService, ownerService, bankAccountService, transactionService);
        mainMenu.run();
    }
}
