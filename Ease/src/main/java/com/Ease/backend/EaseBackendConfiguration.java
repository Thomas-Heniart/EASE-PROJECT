package com.Ease.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


/*
    EaseBackendConfiguration class : will check and load external properties file
 */
public class EaseBackendConfiguration {

    private static final String CONF_ENV_VAR = "JAVA_CONF_PATH_EASE";
    private static final String ERROR_VAR = CONF_ENV_VAR+" is not configured.";
    private static final String ERROR_PATH = CONF_ENV_VAR+" is configured but the file 'config.properties' was not found. Context cannot be launched properly.";
    private static final String ERROR_FILE = CONF_ENV_VAR+" is configured but the file 'config.properties' could not be opened. Context cannot be launched properly.";


    /**
     * Check and get properties object -- read from external properties file
     *
     * @return Properties
     * @throws RuntimeException fails if the file is not found
     * @throws IOException fails if the file is not read properly
     */
    public Properties get() throws RuntimeException {

        // check env var
        final String confPathApp = System.getenv(CONF_ENV_VAR);

        if ( confPathApp == null || confPathApp.isEmpty() ){
            throw new RuntimeException(ERROR_VAR);
        }

        // check is file exists
        if ( !new File(confPathApp + "config.properties").isFile() ){
            throw new RuntimeException(ERROR_PATH);
        }

        // load and return prop object
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream(confPathApp + "config.properties"));
        }catch( IOException e ){
            System.out.println( ERROR_FILE );
            throw new RuntimeException( ERROR_FILE );
        }

        return pro ;

    }

}
