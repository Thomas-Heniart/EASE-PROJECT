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
            //this.session.flush();
            if (!this.transaction.isActive())
                return;
            this.transaction.commit();
            System.out.println("Hibernate transaction commit");
        } catch (RuntimeException e) {
            System.out.println("Hibernate transaction rollback");
            this.transaction.rollback();
            throw new HttpServletException(HttpStatus.InternError, e);
        } /* finally {
            if (this.session != null) {
                System.out.println("Hibernate close session.");
                this.session.close();
            }
        } */
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
        /* if (this.session != null) {
            this.session.close();
        } */
    }

}
