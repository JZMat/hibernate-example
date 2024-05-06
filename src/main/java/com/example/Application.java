package com.example;

import com.example.repository.BankRepository;
import com.example.repository.BankRepositoryImpl;
import com.example.service.BankService;
import com.example.service.BankServiceImpl;
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

        // Create and run the main menu with the BankService instance
        MainMenu mainMenu = new MainMenu(bankService);
        mainMenu.run();
    }
}
