package pe.gob.congreso.configuration;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] { AppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    // @Override
    // protected Filter[] getServletFilters() {
    // return new Filter[] { new HiddenHttpMethodFilter() };
    // }
    //

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] { new CORSFilter() };
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.addListener(new SessionListener());
        servletContext.setInitParameter("org.ajax4jsf.xmlparser.ORDER", "NONE");
        servletContext.setInitParameter("org.ajax4jsf.xmlparser.NONE", ".*");
    }

}
