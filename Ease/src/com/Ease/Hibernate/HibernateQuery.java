package com.Ease.Hibernate;

import com.Ease.Utils.HttpServletException;
import com.Ease.Utils.HttpStatus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by thomas on 20/04/2017.
 */
public class HibernateQuery {

    protected Session session;
    protected Transaction transaction;
    protected Query query;

    public HibernateQuery() {
        this.session = HibernateDatabase.getSessionFactory().getCurrentSession();
        this.transaction = this.session.beginTransaction();
        System.out.println("Hibernate transaction begin");
    }

    public void queryString(String query) {
        this.query = this.session.createQuery(query);
    }

    public void querySQLString(String query) {
        this.query = this.session.createNativeQuery(query);
    }

    public void setMaxResults(int limit) {
        this.query.setMaxResults(limit);
    }

    public void setParameter(String parameter, Object value) {
        this.query.setParameter(parameter, value);
    }

    public void setParameter(int i, Object value) {
        this.query.setParameter(i, value);
    }

    public List list() {
        return this.query.list();
    }

    public void commit() throws HttpServletException {
        try {
            if (!this.transaction.isActive())
                return;
            this.transaction.commit();
            System.out.println("Hibernate transaction commit");
        } catch (RuntimeException e) {
            System.out.println("Hibernate transaction rollback");
            this.transaction.rollback();
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Object load(Class c, Serializable s) {
        return this.session.load(c, s);
    }

    public void saveOrUpdateObject(Object object) {
        this.session.saveOrUpdate(object);
    }

    public void deleteObject(Object object) {
        this.session.delete(object);
    }

    public Object getSingleResult() {
        try {
            List rs = this.query.list();
            if (rs.isEmpty())
                return null;
            else
                return this.query.list().get(0);
        } catch (NoResultException e) {
            return null;
        }
    }

    public int executeUpdate() {
        return this.query.executeUpdate();
    }

    public void rollback() {
        this.transaction.rollback();
    }

    public Object get(Class someClass, Integer id) {
        return this.session.get(someClass, id);
    }

    public boolean isOpen() {
        return this.session.isOpen();
    }
}
