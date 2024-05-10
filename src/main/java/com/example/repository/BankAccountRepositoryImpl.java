package com.example.repository;

import com.example.model.BankAccount;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class BankAccountRepositoryImpl implements BankAccountRepository {


    private final SessionFactory sessionFactory;

    public BankAccountRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public void saveBankAccount(BankAccount bankAccount) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(bankAccount);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {

        try (Session session = sessionFactory.openSession()) {
            Query<BankAccount> query = session.createQuery("SELECT ba FROM BankAccount ba LEFT JOIN FETCH ba.owners", BankAccount.class);
            return query.list();
        }
    }

    @Override
    public void updateBankAccount(BankAccount chosenAccount) {

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(chosenAccount);
            session.getTransaction().commit();
        }
    }


}
