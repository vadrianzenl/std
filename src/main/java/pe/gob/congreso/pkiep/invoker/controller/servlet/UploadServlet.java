package pe.gob.congreso.pkiep.invoker.controller.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;


@WebServlet(name = "/UploadServlet", urlPatterns="/uploadServlet")
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String UPLOAD_DIRECTORY = "upload";
	private static final int THRESHOLD_SIZE 	= 1024 * 1024 * 3; 	// MB
	private static final int MAX_FILE_SIZE 		= 1024 * 1024 * 100; // MB
	private static final int MAX_REQUEST_SIZE 	= 1024 * 1024 * 110; // MB 
	
	public UploadServlet() {
		super();
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter writer =  response.getWriter();
		writer.write(System.getProperty("java.io.tmpdir"));
		writer.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {    	  	
    	try {	    	
			if (!ServletFileUpload.isMultipartContent(request)) {	
				response.setStatus(HttpServletResponse.SC_PRECONDITION_FAILED);	
				return;
			}	
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			factory.setSizeThreshold(THRESHOLD_SIZE);
			factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
			
			ServletFileUpload upload = new ServletFileUpload(factory);
			upload.setFileSizeMax(MAX_FILE_SIZE);
			upload.setSizeMax(MAX_REQUEST_SIZE);
						
			//String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIRECTORY;			
			String uploadPath = System.getProperty("java.io.tmpdir") + File.separator + UPLOAD_DIRECTORY;
			//System.out.println("UploadPath -> " + uploadPath);
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}				
			
			List<FileItem> formItems = upload.parseRequest(request);
			Iterator<FileItem> iter = formItems.iterator();						
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();				
				if (!item.isFormField()) {
					String idFile = item.getFieldName();//idFile asignado en los argumentos, se puede utilizar como un id.	
					String fileName = URLDecoder.decode(item.getName(),"UTF-8");						
					String filePath = uploadPath + File.separator + fileName;
					File storeFile = new File(filePath);			
					item.write(storeFile);
				}
			}			
			response.setStatus(HttpServletResponse.SC_OK);			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);	
		}		
    }
}
