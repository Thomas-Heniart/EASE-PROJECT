package com.Ease.Hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/**
 * Created by thomas on 20/04/2017.
 */
public class HibernateDatabase {
    private static final SessionFactory sessionFactory;
    private static final SessionFactory sessionFactory1;

    static {
        try {
            StandardServiceRegistry standardRegistry =
                    new StandardServiceRegistryBuilder().configure().build();
            Metadata metaData =
                    new MetadataSources(standardRegistry).getMetadataBuilder().build();
            sessionFactory = metaData.getSessionFactoryBuilder().build();

            StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder().configure("hibernate-tracking.cfg.xml").build();
            Metadata metadata = new MetadataSources(standardServiceRegistry).getMetadataBuilder().build();
            sessionFactory1 = metadata.getSessionFactoryBuilder().build();
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
