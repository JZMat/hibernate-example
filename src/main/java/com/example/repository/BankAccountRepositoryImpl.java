package com.example.repository;

import com.example.model.Bank;
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
            Query<BankAccount> query = session.createQuery("FROM BankAccount", BankAccount.class);
            return query.list();
        }

    }

}
