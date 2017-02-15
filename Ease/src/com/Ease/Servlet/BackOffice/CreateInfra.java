package com.Ease.Servlet.BackOffice;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.Ease.Context.Variables;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class CreateInfra
 */
@WebServlet("/CreateInfra")
public class CreateInfra extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateInfra() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			} else if (!ServletFileUpload.isMultipartContent(request)) {
				// if not, w stop here
				sm.setResponse(ServletManager.Code.ClientWarning, "Wrong files.");
			} else {
				// configures upload settings
				DiskFileItemFactory factory = new DiskFileItemFactory();
				// sets memory threshold - beyond which files are stored in disk
				factory.setSizeThreshold(MEMORY_THRESHOLD);
				// sets temporary location to store files
				factory.setRepository(new File(System.getProperty("java.io.tmpdir")));

				ServletFileUpload upload = new ServletFileUpload(factory);

				// sets maximum size of upload file
				upload.setFileSizeMax(MAX_FILE_SIZE);

				// sets maximum size of request (include file + form data)
				upload.setSizeMax(MAX_REQUEST_SIZE);

				// constructs the directory path to store upload file
				// this path is relative to application's directory
				String uploadPath = "";
				String infraName = "";
				String filePath = "";
				// creates the directory if it does not exist

				// parses the request's content to extract file data
				List<FileItem> formItems = upload.parseRequest(request);
				if (formItems != null && formItems.size() > 0) {
					// iterates over form's fields
					for (FileItem item : formItems) {
						if (item.isFormField()) {
							if (item.getFieldName().equals("infraName")) {
								infraName = item.getString();
								if (infraName == null || infraName.length() > 25) {
									throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong infrastructure name");
								}
								uploadPath = Variables.PROJECT_PATH + Variables.WEBSITES_PATH + item.getString();
							}
						}
						// processes only fields that are not form fields
						if (!item.isFormField()) {
							uploadPath = Variables.PROJECT_PATH + "/resources/images/infras/";
							String fileName = new File(item.getName()).getName();
							File storeFile;
							if (fileName.endsWith(".png")) {
								File uploadDir = new File(uploadPath);
								if (!uploadDir.exists()) {
									uploadDir.mkdir();
								}
								filePath = uploadPath + File.separator + infraName + ".png";
								storeFile = new File(filePath);
								if (storeFile.exists())
									storeFile.renameTo(new File(uploadPath + File.separator + "old_"+ infraName + ".png"));
								item.write(storeFile);
							}
							Infrastructure infra = Infrastructure.createInfrastructure(infraName, infraName + ".png", sm);
							sm.setResponse(ServletManager.Code.Success, Integer.toString(infra.getSingleId()));
						}
					}
				}
			}
			
			
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}
