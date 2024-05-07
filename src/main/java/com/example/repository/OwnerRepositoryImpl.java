package com.example.repository;

import com.example.model.Owner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class OwnerRepositoryImpl implements OwnerRepository {


    private final SessionFactory sessionFactory;

    public OwnerRepositoryImpl(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOwner(Owner owner) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(owner);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Owner> getAllOwners() {
        try (Session session = sessionFactory.openSession()) {
            Query<Owner> query = session.createQuery("FROM Owner", Owner.class);
            return query.list();
        }
    }
}
