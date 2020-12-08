package pe.gob.congreso.pkiep.invoker.util;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class Configuration {
	//PROPERTIES
    private static final String DEFAULT_PROPERTY_FILE = "default.properties"; 
    //
    private static final String CLIENT_ID = "clientId";
    private static final String CLIENT_SECRET = "clientSecret";
    private static final String PROTOCOL = "Protocol";
   
    private static Configuration instance;
    private static Properties defaultProperties;
    //    
    
    public static Configuration getInstance() {
        if (instance == null) {
            instance = new Configuration();
        }
        return instance;
    }
    
    private Configuration() {
        defaultProperties = loadDefaultProperties(); 
        
    }
    
    private Properties loadDefaultProperties() {
        String resourceName = "/" + DEFAULT_PROPERTY_FILE;
        System.out.println("Loading " + resourceName);
        InputStream fis = null;
        Properties prop = new Properties();
        try {
            fis = this.getClass().getResourceAsStream(resourceName);
            prop.load(fis);
        } catch (IOException ioe) {
        	System.err.println(ioe.getStackTrace().toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                	System.err.println(e.getStackTrace().toString());
                }
            }
        }
        return prop;
    }
    
    /*PROPERTIES*/ 
    public String getClientId() {
        return defaultProperties.getProperty(CLIENT_ID);
    }
    public String getClientSecret() {
        return defaultProperties.getProperty(CLIENT_SECRET);
    }
    public String getProtocol() {
        return defaultProperties.getProperty(PROTOCOL);
    }

}
