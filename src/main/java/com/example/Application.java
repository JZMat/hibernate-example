package com.example;

import com.example.repository.BankRepository;
import com.example.repository.BankRepositoryImpl;
import com.example.repository.OwnerRepository;
import com.example.repository.OwnerRepositoryImpl;
import com.example.service.BankService;
import com.example.service.BankServiceImpl;
import com.example.service.OwnerService;
import com.example.service.OwnerServiceImpl;
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

        // Create and run the main menu with the BankService instance and OwnerService instance
        MainMenu mainMenu = new MainMenu(bankService, ownerService);
        mainMenu.run();
    }
}
