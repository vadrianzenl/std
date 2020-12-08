package pe.gob.congreso.pkiep.invoker.controller.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pe.gob.congreso.pkiep.invoker.util.Configuration;


@WebServlet(name = "/ArgumentsServlet", urlPatterns="/argumentsServlet")
public class ArgumentsServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Configuration config = Configuration.getInstance();	
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer =  response.getWriter();
		writer.write(UUID.randomUUID().toString() + ".pdf");
		writer.close();
    }
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String pathServlet = ""; //request.getServletPath();
        String fullPathServlet = ""; //request.getRequestURL().toString();
        int resInt = fullPathServlet.length() - pathServlet.length();
        String serverPath = fullPathServlet.substring(0, resInt + 1);
        if(!serverPath.contains("localhost")){        
        	// EN PRODUCCIÓN: config.getProtocol() define el protocolo.
        	serverPath = config.getProtocol() + "://" + serverPath.replace("http://", "").replace("https://", "");
        }
        
		PrintWriter writer =  response.getWriter();
        try{
           String type = ""; //request.getParameter("type").toString();         
           String documentName = ""; //request.getParameter("documentName").toString(); 
           //Agregado AEP 
           String motivo = ""; //request.getParameter("motivo").toString();
           //Fin Agregado AEP
           String arguments = "";
           
           String protocol = "";
           if (serverPath.contains("https://")){
        	   protocol = "S";
           }else{
        	   protocol = "T";
           }
           
           if(type.equals("W")){
        	   arguments = paramWeb(protocol, serverPath, documentName);
           }else if(type.equals("L")){
        	   arguments = paramLocal(protocol, serverPath, documentName, motivo); //Modificado Parametro AEP 28.06.2019
           }           
           writer.write(arguments);
           writer.close();
           }
       catch(Exception ex)
      {
    	   ex.getStackTrace();
      }
	}
	
	public String paramWeb(String protocol, String ServerPath, String documentName){		
		String param = "";
        ObjectMapper mapper = new ObjectMapper();        
        //Java to JSON
        try {
            Map<String, String> map = new HashMap<>();
            map.put("app", "pdf");
            map.put("clientId", config.getClientId());   
            map.put("clientSecret", config.getClientSecret());         
            map.put("idFile", "001"); //FieldName Multipart, al momento de recibir el archivo subido se puede utilizar este argumento como identificador.
            map.put("type", "W"); //L=Documento está en la PC , W=Documento está en la Web.
            map.put("protocol", protocol); //T=http, S=https (SE RECOMIENDA HTTPS)
            map.put("fileDownloadUrl", ServerPath + "documents/demo.pdf");
            map.put("fileDownloadLogoUrl", ServerPath + "assets/images/iLogo1.png");
            map.put("fileDownloadStampUrl", ServerPath + "assets/images/iFirma1.png");
            map.put("fileUploadUrl", ServerPath + "uploadServlet");      
            map.put("contentFile", "demo.pdf");
            map.put("reason", "Soy el autor del documento");
            map.put("isSignatureVisible", "true");             
            map.put("stampAppearanceId", "0"); //0:(sello+descripcion) horizontal, 1:(sello+descripcion) vertical, 2:solo sello, 3:solo descripcion
            map.put("pageNumber", "0");
            map.put("posx", "5"); 
            map.put("posy", "5");                 
            map.put("fontSize", "7"); 
            map.put("dcfilter", ".*FIR.*|.*FAU.*"); //".*" todos, solo firma ".*FIR.*|.*FAU.*"
            map.put("timestamp", "true");               
            map.put("outputFile", documentName);    
            map.put("maxFileSize", "5242880"); //Por defecto será 5242880 5MB - Maximo 100MB
            //JSON
            param = mapper.writeValueAsString(map);  
            System.out.println("******* PARAMETROS **********");
            System.out.println(param);
                      
            //Base64 (JAVA 8)
            param = Base64.getEncoder().encodeToString(param.getBytes(StandardCharsets.UTF_8));           
            System.out.println(param);             
        } catch (JsonProcessingException ex) {            
        }	
        
        return param;
	}
	
	public String paramLocal(String protocol, String ServerPath, String documentName, String motivo){		
		String param = "";
        ObjectMapper mapper = new ObjectMapper();        
        //Java to JSON
        try {
            Map<String, String> map = new HashMap<>();
            map.put("app", "pdf");
            map.put("clientId", config.getClientId());   
            map.put("clientSecret", config.getClientSecret());
            map.put("idFile", "002"); //FieldName Multipart, al momento de recibir el archivo subido se puede utilizar este argumento como identificador.
            map.put("type", "L"); //L=Documento está en la PC , W=Documento está en la Web.
            map.put("protocol", protocol); //T=http, S=https (SE RECOMIENDA HTTPS)
            map.put("fileDownloadUrl", "");
            map.put("fileDownloadLogoUrl", ServerPath + "assets/images/iLogo1.png");
            map.put("fileDownloadStampUrl", ServerPath + "assets/images/iFirma1.png");
            map.put("fileUploadUrl", (ServerPath + "uploadServlet"));                    
            map.put("contentFile", "");
            //map.put("reason", "Autor del documento");
            System.out.println("****Cargando motivo como parámetro****" + motivo);
            map.put("reason", motivo);
            map.put("isSignatureVisible", "true");            
            map.put("stampAppearanceId", "3"); //0:(sello+descripcion) horizontal, 1:(sello+descripcion) vertical, 2:solo sello, 3:solo descripcion
            map.put("pageNumber", "0");
            map.put("posx", "175");
            map.put("posy", "8");                
            map.put("fontSize", "6"); 
            map.put("dcfilter", ".*FIR.*|.*FAU.*"); //".*" todos, solo fir ".*FIR.*|.*FAU.*"
            map.put("timestamp", "false");               
            map.put("outputFile", documentName);  
            map.put("maxFileSize", "5242880"); //Por defecto será 5242880 5MB - Maximo 100MB            
            //JSON
            param = mapper.writeValueAsString(map);    
            System.out.println("JSON: " + param);   
            
            //Base64 (JAVA 8)
            param = Base64.getEncoder().encodeToString(param.getBytes(StandardCharsets.UTF_8));           
            System.out.println("BASE64: " + param);              
        } catch (JsonProcessingException ex) {            
        }	
        
        return param;
	}

}
