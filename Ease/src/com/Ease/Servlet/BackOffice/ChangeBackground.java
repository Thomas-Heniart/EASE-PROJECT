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

import com.Ease.Dashboard.User.User;

/**
 * Servlet implementation class UploadWebsite
 */
@WebServlet("/changeBackground")
public class ChangeBackground extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "/var/lib/tomcat7/webapps/backgrounds";
	// private static final String UPLOAD_DIRECTORY =
	// "/Users/thomas/EASE-PROJECT/Ease/WebContent/resources/websites";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChangeBackground() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();

		User user = (User) session.getAttribute("User");

		if (!user.isAdmin()) {
			response.getWriter().print("error: You aint admin bro");
			return;
		}

		if (!ServletFileUpload.isMultipartContent(request)) {
			// if not, we stop here
			response.getWriter().print("error: Form must has enctype=multipart/form-data.");
			return;
		}

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
		String uploadPath = UPLOAD_DIRECTORY;

		// creates the directory if it does not exist

		try {
			// parses the request's content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);
			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					if (!item.isFormField()) {
						String filePath;
						File storeFile;
						filePath = uploadPath + "/" + "background.jpeg";
						storeFile = new File(filePath);
						if (storeFile.exists()) {
							String newPathName = uploadPath + "/" + "background_old";
							File temp = new File(newPathName + ".jpeg");
							while (temp.exists()) {
								newPathName += "_old";
								temp = new File(newPathName + ".jpeg");
							}
							storeFile.renameTo(temp);
						}

						item.write(storeFile);
					}
				}
			}
		} catch (Exception ex) {
			response.getWriter().print("error: " + ex);
		}
		// redirects client to message page
		response.getWriter().print("success");
	}
}
