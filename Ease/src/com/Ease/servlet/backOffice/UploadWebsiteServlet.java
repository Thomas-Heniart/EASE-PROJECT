package com.Ease.servlet.backOffice;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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

import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class UploadWebsite
 */
@WebServlet("/uploadWebsite")
public class UploadWebsiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// location to store file uploaded
	private static final String UPLOAD_DIRECTORY = "/var/lib/tomcat7/webapps/ROOT/resources/websites";
	// private static final String UPLOAD_DIRECTORY =
	// "/Users/thomas/EASE-PROJECT/Ease/WebContent/resources/websites";

	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadWebsiteServlet() {
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

		if (!user.isAdmin(session.getServletContext())) {
			response.getWriter().print("error: You aint admin bro");
			return;
		}

		if (!ServletFileUpload.isMultipartContent(request)) {
			// if not, we stop here
			PrintWriter writer = response.getWriter();
			writer.println("Error: Form must has enctype=multipart/form-data.");
			writer.flush();
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
		String uploadPath = "";

		// creates the directory if it does not exist

		try {
			// parses the request's content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);

			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					if (item.isFormField()) {
						if (item.getFieldName().equals("siteName")) {
							uploadPath = UPLOAD_DIRECTORY + File.separator + item.getString();
							File uploadDir = new File(uploadPath);
							if (!uploadDir.exists()) {
								uploadDir.mkdir();
							}
						}
					}
					// processes only fields that are not form fields
					if (!item.isFormField()) {
						// System.out.println(uploadPath);
						String fileName = new File(item.getName()).getName();
						String filePath;
						File storeFile;
						if (fileName.endsWith(".json")) {

							filePath = uploadPath + File.separator + "connect.json";
							// System.out.println(filePath);
							storeFile = new File(filePath);
							if (storeFile.exists())
								storeFile.renameTo(new File(uploadPath + File.separator + "connect_old.json"));

							// saves the file on disk
							Stats.saveAction(session.getServletContext(), user, Stats.Action.UploadWebsite,
									"Uploaded " + filePath);
							item.write(storeFile);
						}
						if (fileName.endsWith(".png")) {
							filePath = uploadPath + File.separator + "logo.png";
							storeFile = new File(filePath);
							if (storeFile.exists())
								storeFile.renameTo(new File(uploadPath + File.separator + "logo_old.png"));

							Stats.saveAction(session.getServletContext(), user, Stats.Action.UploadWebsite,
									"Uploaded " + filePath);
							item.write(storeFile);

						}
						request.setAttribute("message", "Upload has been done successfully!");
					}
				}
			}
		} catch (Exception ex) {
			request.setAttribute("message", "There was an error: " + ex.getMessage());
		}
		// redirects client to message page
		this.doGet(request, response);
	}

}
