package com.example.repository;

import com.example.model.Bank;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BankRepositoryImpl implements BankRepository {

    private final SessionFactory sessionFactory;

    public BankRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBank(Bank bank) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // session.save(bank); <-- wycofane
            // Persist the Bank entity
            session.persist(bank);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Bank> getAllBanks() {
        System.out.println("In the the getAllBanks() of the BankRepositoryImpl class");
        try (Session session = sessionFactory.openSession()) {
            Query<Bank> query = session.createQuery("FROM Bank", Bank.class);
            System.out.println("In the try in the getAllBanks() of the BankRepositoryImpl class");
            return query.list();
        }
    }
}
