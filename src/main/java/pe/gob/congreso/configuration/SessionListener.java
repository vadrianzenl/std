package pe.gob.congreso.configuration;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionListener implements HttpSessionListener {
	
	protected final Log log = LogFactory.getLog(getClass());

    @Override
    public void sessionCreated(HttpSessionEvent hse) {        
        log.info("==== Session creada ====");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
    	log.info("==== Session terminada ====");         
    }

}

