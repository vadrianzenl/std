package pe.gob.congreso.pkiep.invoker.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "/DeleteFilesServlet", urlPatterns="/deleteFilesServlet")
public class DeleteFilesServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SEPARATOR = System.getProperty("file.separator");
	private static final String UPLOAD_DIRECTORY = "upload";
		
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer =  response.getWriter();		
		//String uploadPath = request.getServletContext().getRealPath("") + SEPARATOR + UPLOAD_DIRECTORY;
		String uploadPath = System.getProperty("java.io.tmpdir") + SEPARATOR + UPLOAD_DIRECTORY; 
		File UploadDir = new File(uploadPath);
        if (!UploadDir.exists()) return;         
        for (File file : UploadDir.listFiles()) {
        	writer.write("Eliminando: " + file.getName());
            deleteFileAndDir(file);
        }    
        writer.close();
		response.setStatus(HttpServletResponse.SC_OK);		
    }
	
	private void deleteFileAndDir(File file) {
        if (!file.exists()) return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFileAndDir(f);  
            }
        }
        file.delete();
    } 

}
