package com.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.example.model.Bank;

public class Main {
    public static void main(String[] args) {
        // Create a configuration instance
        Configuration cfg = new Configuration();

        // Load Hibernate configuration file
        cfg.configure("hibernate.cfg.xml");

        // Build a SessionFactory
        SessionFactory sessionFactory = cfg.buildSessionFactory();

        // Open a new Session
        Session session = sessionFactory.openSession();

        try {
            // Begin transaction
            session.beginTransaction();

            // Create a new Bank entity
            Bank newBank = new Bank();
            newBank.setBank_name("Test Bank");

            // Persist the Bank entity
            session.persist(newBank);

            session.flush();

            // Commit the transaction
            session.getTransaction().commit();

            System.out.println("Bank entity saved with id: " + newBank.getBank_id());
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                // Rollback the transaction in case of error
                session.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            // Close the Session
            session.close();
        }

        // Close the SessionFactory
        sessionFactory.close();
    }
}
