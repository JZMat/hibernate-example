package com.example.repository;

import com.example.model.Transaction;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final SessionFactory sessionFactory;

    public TransactionRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            session.beginTransaction();
            transaction = session.get(Transaction.class, transactionId);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return transaction;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.merge(transaction);
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<Transaction> getTransactionsByBankAccount(int accountId) {
        Session session = sessionFactory.openSession();
        List<Transaction> transactions = new ArrayList<>(); // Initialize to an empty list
        try {
            session.beginTransaction();
            Query<Transaction> query = session.createQuery("from Transaction where sourceAccount.account_id=:accountId or destinationAccount.account_id=:accountId", Transaction.class);
            query.setParameter("accountId", accountId);
            transactions = query.getResultList();
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
        } finally {
            session.close();
        }
        return transactions;
    }

}
