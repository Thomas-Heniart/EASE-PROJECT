package com.Ease.backend;

import com.sun.beans.finder.ClassFinder;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

/*
    HibernateConfiguration class : will load hibernate connection via external properties file
                                   and use former cfg file for mappings class only
 */
public class HibernateConfiguration {

    private static final String TYPE_DB = "TYPE_DB";
    private static final String TYPE_METRICS = "TYPE_METRICS";

    private static final String FILE_DB = "hibernate.cfg.xml";
    private static final String FILE_METRICS = "hibernate-tracking.cfg.xml";


    /**
     * Build SessionFactory
     * @param prop main app 'config.properties' properties file to read
     * @param type type of session you want DB or METRICS
     * @return SessionFactory
     * @throws ClassNotFoundException, this will abort the server to start when hit
     */
    public SessionFactory buildSessionFactory(Properties prop, String type ) throws ClassNotFoundException {

        Configuration c = new Configuration();
        c.addProperties( prop );
        addMappings( c, type );

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(c.getProperties());
        SessionFactory factory = c.buildSessionFactory(builder.build());

        return factory;

    }


    private void addMappings( Configuration c, String type ) throws ClassNotFoundException {

        switch( type ){
            case TYPE_DB:
                addMappingsFromXml(c, FILE_DB);
                break;
            case TYPE_METRICS:
                addMappingsFromXml(c, FILE_METRICS);
                //cache exceptions for metrics
                c.setProperty("hibernate.cache.use_second_level_cache","false");
                c.setProperty("hibernate.cache.use_query_cache","false");
                break;
        }

    }

    private void addMappingsFromXml( Configuration c, String file ) throws  ClassNotFoundException {

        String classes = convertStreamToString(this.getClass().getClassLoader().getResourceAsStream(file) );
        classes = classes;

        String[] lines = classes.split("\n");

        for ( String line : lines){

            line = line.trim();
            if ( line.startsWith( "<mapping class=\"")){

                int posEnd = line.indexOf( "/>" );
                if ( posEnd > 0 ) {
                    String classToAdd =line.substring( 16,posEnd -1 );
                    c.addAnnotatedClass( ClassFinder.findClass( classToAdd ));
                }
            }

        }

    }

    private String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
