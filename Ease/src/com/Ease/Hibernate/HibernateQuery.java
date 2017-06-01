package com.Ease.Hibernate;

import com.Ease.Utils.ServletManager;
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
        this.session = HibernateDatabase.getSessionFactory().openSession();
        this.transaction = this.session.beginTransaction();
    }

    public void queryString(String query) {
        this.query = this.session.createQuery(query);
    }

    public void querySQLString(String query) {
        this.query = this.session.createNativeQuery(query);
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

    public void commit() {
        try {
            this.session.flush();
            this.transaction.commit();
        } catch (RuntimeException e) {
            this.transaction.rollback();
        } finally {
            if (this.session != null) {
                this.session.close();
            }
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
            return this.query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public int executeUpdate() {
        return this.query.executeUpdate();
    }

    public void rollback() {
        this.transaction.rollback();
        if (this.session != null) {
            this.session.close();
        }
    }

}
