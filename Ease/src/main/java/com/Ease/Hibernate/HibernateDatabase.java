package com.Ease.Hibernate;

import com.Ease.backend.common.configuration.ApplicationConfiguration;
import com.Ease.backend.common.configuration.HibernateConfiguration;
import org.hibernate.SessionFactory;

/**
 * Created by thomas on 20/04/2017.
 */
public class HibernateDatabase {
    private static final SessionFactory sessionFactory;
    private static final SessionFactory sessionFactory1;

    private static final String TYPE_DB = "TYPE_DB";
    private static final String TYPE_METRICS = "TYPE_METRICS";

    static {
        try {

            // standard hibernate config
            sessionFactory = new HibernateConfiguration().buildSessionFactory( new ApplicationConfiguration().get(), TYPE_DB);
            // metrics hibernate config
            sessionFactory1 = new HibernateConfiguration().buildSessionFactory( new ApplicationConfiguration().get(), TYPE_METRICS);

        } catch (Throwable th) {
            System.err.println("Initial SessionFactory creation failed" + th);
            throw new ExceptionInInitializerError(th);
        }
    }

    public synchronized static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public synchronized static SessionFactory getTrackingSessionFactory() {
        return sessionFactory1;
    }

    public static void close() {
        sessionFactory1.close();
        sessionFactory.close();
    }
}
