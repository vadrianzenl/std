package pe.gob.congreso.pkiep.invoker.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "/GetFileServlet", urlPatterns="/getFileServlet")
public class GetFileServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	private static final String UPLOAD_DIRECTORY = "upload";
		
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String documentName = "tempo"; //request.getParameter("documentName").toString(); 
		
		Path path = Paths.get(System.getProperty("java.io.tmpdir") + File.separator + UPLOAD_DIRECTORY + File.separator + documentName);
		byte[] data = Files.readAllBytes(path);						
		
		response.setContentType ("application/pdf");	
		response.setHeader("Content-disposition", "attachment; filename=" + documentName);	
		response.setHeader("Cache-Control", "max-age=30");
        response.setHeader("Pragma", "No-cache");
        response.setDateHeader("Expires",0);
        response.setContentLength(data.length);
        
        ServletOutputStream out = response.getOutputStream();
        out.write(data,0,data.length);
        out.flush();
        out.close();
        
        
    }
	
	

}
