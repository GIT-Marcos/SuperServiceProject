package org.superservice.superservice.DAOs;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.superservice.superservice.utilities.TransactionExecutor;
import org.superservice.superservice.utilities.Util;

import java.util.ArrayList;
import java.util.List;

public class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    private final Class<T> entityClass;

    public GenericDAOImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    //TODO: pasar esto como función.
    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        Transaction tx = null;
        try (Session session = Util.getHibernateSessionThreadSafe()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<T> criteria = builder.createQuery(entityClass);
            criteria.from(entityClass);
            tx = session.beginTransaction();
            list = session.createQuery(criteria).list();
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            Util.cerrarHibernateSessionThreadSafe();
        }
        return list;
    }

    @Override
    public T getById(ID id) {
        return TransactionExecutor.executeInTransaction(session -> session.find(entityClass, id));
    }

    @Override
    public void save(T entity) {
        TransactionExecutor.executeInTransaction(session -> {
            session.persist(entity);
            return null;
        });
    }

    @Override
    public void update(T entity) {
        TransactionExecutor.executeInTransaction(session -> {
            session.merge(entity);
            return null;
        });
    }

    @Override
    public void delete(T entity) {
        TransactionExecutor.executeInTransaction(session -> {
            session.remove(entity);
            return null;
        });
    }
}
