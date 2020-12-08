package pe.gob.congreso.util;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileHelper {

    public String responseBody;

    protected final Log log = LogFactory.getLog(getClass());

    public byte[] toFileByte(File file) throws IOException, FileNotFoundException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = new FileInputStream(file);
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    public byte[] toInputStreamByte(InputStream con) throws IOException {
        ByteArrayOutputStream ous = null;
        InputStream ios = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            ios = con;
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null) {
                    ous.close();
                }
            } catch (IOException e) {
                log.info("Error : " + e.getMessage());
            }

            try {
                if (ios != null) {
                    ios.close();
                }
            } catch (IOException e) {
                log.info("Error : " + e.getMessage());
            }
        }
        return ous.toByteArray();
    }

    public byte[] getFileAlfresco(String ruta, String spaceStore, String site, String user, String password, String documento) throws IOException {
        byte[] bytes;
        InputStream fileAlfresco = this.getFile(ruta, spaceStore, site, user, password, documento);
        bytes = this.toInputStreamByte(fileAlfresco);
        return bytes;
    }

    public InputStream getFile(String ruta, String spaceStore, String site, String user, String password, String documento) throws IOException {
        final String use = user;
        final String pas = password;
        InputStream input;

        URL url = new URL(ruta + "/alfresco/service/api/node/content/workspace/SpacesStore/"+ documento);
        URLConnection con = url.openConnection();
        Authenticator au = new Authenticator() {
            @Override
            protected PasswordAuthentication
            getPasswordAuthentication() {
                return new PasswordAuthentication(use, pas.toCharArray());
            }
        };
        Authenticator.setDefault(au);
        input = con.getInputStream();
        return input;
    }

    public void uploadFileAlfresco(InputStream inputStream, String ruta, String site, String filename, String user, String password) {
        log.debug("uploadFileAlfresco()");
        OutputStream salida = null;
        try {

            String alfrescoTiccketURL = ruta + "/alfresco" + "/service/api/login?u=" + user + "&pw=" + password;

            InteractiveAuthentication ticket = new InteractiveAuthentication();
            String authTicket = ticket.getTicket(alfrescoTiccketURL);

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String fechaDia = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
            String fechaMes = String.format("%02d", cal.get(Calendar.MONTH));
            String fechaHoraIni = String.valueOf(cal.get(Calendar.YEAR)) + "-" + fechaMes + "-" + fechaDia + "-" + String.valueOf(cal.get(Calendar.HOUR_OF_DAY)) + "-" + String.valueOf(cal.get(Calendar.MINUTE));

            //String nameFile = "/tmp/temp" + fechaHoraIni + ".pdf";
            String nameFile = "temp" + fechaHoraIni + ".pdf";

            File fileobj = new File(nameFile);
            //Para Windows
            //File fileobj = new File("temp.pdf");
            salida = new FileOutputStream(fileobj);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                salida.write(buf, 0, len);
            }

            String urlString = ruta + "/alfresco/service/api/upload?alf_ticket=" + authTicket;
            log.debug("The upload url:::" + urlString);
            HttpClient client = new HttpClient();

            PostMethod mPost = new PostMethod(urlString);
            log.debug("FileName:::" + filename);
            log.debug("FileObj:::" + fileobj);

            Part[] parts = {
                    new FilePart("filedata", filename, fileobj, "application/pdf", null),
                    new StringPart("filename", filename),
                    new StringPart("description", "description"),
                    new StringPart("siteid", site),
                    new StringPart("containerid", "documentLibrary"),
            };

            mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost.getParams()));
            int statusCode1 = client.executeMethod(mPost);
            String responseBody = mPost.getResponseBodyAsString();
            System.out.println("statusLine>>>" + statusCode1 + "......"
                    + "\n status line \n"
                    + mPost.getStatusLine() + "\nbody \n" + responseBody);
            this.setResponseBody(responseBody);
            mPost.releaseConnection();

            fileobj.delete();

        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        } finally{
            try {
                salida.close();
                inputStream.close();
            } catch (IOException ex) {
                log.error("Error : " + ex.getMessage());
            }
        }
    }

    public void uploadFile(String ruta, String site, String user, String password, String path, String filename, String mimeType) {
        log.debug("uploadFileAlfresco()");
        try {

            String alfrescoTiccketURL = ruta + "/alfresco" + "/service/api/login?u=" + user + "&pw=" + password;

            InteractiveAuthentication ticket = new InteractiveAuthentication();
            String authTicket = ticket.getTicket(alfrescoTiccketURL);
            File fileobj = new File(path);

            String urlString = ruta + "/alfresco/service/api/upload?alf_ticket=" + authTicket;
            log.debug("The upload url:::" + urlString);
            HttpClient client = new HttpClient();

            PostMethod mPost = new PostMethod(urlString);
            log.debug("FileName:::" + filename);
            log.debug("FileObj:::" + fileobj);

            Part[] parts = {
                    new FilePart("filedata", filename, fileobj, mimeType, null),
                    new StringPart("filename", filename),
                    new StringPart("description", "description"),
                    new StringPart("siteid", site),
                    new StringPart("containerid", "documentLibrary"),
            };

            mPost.setRequestEntity(new MultipartRequestEntity(parts, mPost.getParams()));
            int statusCode1 = client.executeMethod(mPost);
            String responseBody = mPost.getResponseBodyAsString();
            System.out.println("statusLine>>>" + statusCode1 + "......"
                    + "\n status line \n"
                    + mPost.getStatusLine() + "\nbody \n" + responseBody);
            this.setResponseBody(responseBody);
            mPost.releaseConnection();

            fileobj.delete();

        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    public String getUUID(){
        String uuid="";
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(this.getResponseBody());
            String nodeRef = (String) jsonObject.get("nodeRef");
            uuid = nodeRef.substring(24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return uuid;
    }

}