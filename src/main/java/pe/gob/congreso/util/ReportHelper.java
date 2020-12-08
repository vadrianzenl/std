package pe.gob.congreso.util;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.JREmptyDataSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import java.io.InputStream;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

public class ReportHelper {

    protected final Log log = LogFactory.getLog(getClass());
    Connection conn = null;

    public ByteArrayOutputStream generate(String reportUrl, Map params, ReportType.FILE fileType, ReportType.DATASOURCE dataSource, String url, String driver, String user, String password) {
        try {
            //InputStream reportStream = this.getClass().getResourceAsStream(reportUrl);
            
            InputStream reportStream = new FileInputStream(new File(reportUrl));
            
            JasperDesign jd = JRXmlLoader.load(reportStream);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = null;
            if (dataSource == ReportType.DATASOURCE.WITH) {
                try {
                    //Class.forName("com.mysql.jdbc.Driver");
                    //conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/licencias","root","");
                    Class.forName(driver);
                    conn = (Connection) DriverManager.getConnection(url, user, password);
                    //conn = (Connection) DriverManager.getConnection("jdbc:sqlserver://localhost;databaseName=RRHH;integratedSecurity=true","","");
                    log.info("Conexion OK");
                } catch (ClassNotFoundException ex) {
                    log.info("ERROR NO DRIVER: " + ex.getMessage());
                } catch (SQLException ex) {
                    log.info("ERROR SQLException: " + ex.getMessage());
                }
                jp = JasperFillManager.fillReport(jr, params, conn);
            } else {
                jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (fileType == ReportType.FILE.PDF) {
                ReportExporter.exportToPDF(jp, baos);
            }
            if (fileType == ReportType.FILE.XLS) {
                ReportExporter.exportToXLS(jp, baos);
            }
            if (conn != null) {
                conn.close();
            }
            return baos;
        } catch (SQLException | JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

        public ByteArrayOutputStream generateDataSource(String reportUrl, Map params, ReportType.FILE fileType, ReportType.DATASOURCE dataSource) {
        try {
            //InputStream reportStream = this.getClass().getResourceAsStream(reportUrl);
            
            InputStream reportStream = new FileInputStream(new File(reportUrl));
            
            JasperDesign jd = JRXmlLoader.load(reportStream);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = null;
            if (dataSource == ReportType.DATASOURCE.WITH) {                
                JndiDataSourceLookup lookup = new JndiDataSourceLookup();                
                jp = JasperFillManager.fillReport(jr, params, lookup.getDataSource("java:/MSSQLDS").getConnection());
                
            } else {
                jp = JasperFillManager.fillReport(jr, params, new JREmptyDataSource());
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (fileType == ReportType.FILE.PDF) {
                ReportExporter.exportToPDF(jp, baos);
            }
            if (fileType == ReportType.FILE.XLS) {
                ReportExporter.exportToXLS(jp, baos);
            }
            if (conn != null) {
                conn.close();
            }
            return baos;
        } catch (SQLException | JRException e) {
            e.printStackTrace();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ReportHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private static class ReportExporter {

        public static void exportToPDF(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
            JRPdfExporter exporter = new JRPdfExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

            exporter.exportReport();
        }

        public static void exportToXLS(JasperPrint jp, ByteArrayOutputStream baos) throws JRException {
            JRXlsExporter exporter = new JRXlsExporter();

            exporter.setParameter(JRExporterParameter.JASPER_PRINT, jp);
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            

            exporter.exportReport();
        }

    }

}
