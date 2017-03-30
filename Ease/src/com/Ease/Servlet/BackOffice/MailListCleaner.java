package com.Ease.Servlet.BackOffice;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.GrowthHackingSender;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class MailListCleaner
 */
@WebServlet("/MailListCleaner")
public class MailListCleaner extends HttpServlet {
	private static final long serialVersionUID = 1L;
	// upload settings
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 3; // 3MB
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 40; // 40MB
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 50; // 50MB
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MailListCleaner() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		DataBaseConnection db = sm.getDB();
		try {
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You ain't admin dude");
			DatabaseRequest db_request = db.prepareRequest("SELECT email FROM testingEmails;");
			DatabaseResult rs = db_request.get();
			JSONArray emails = new JSONArray();
			while (rs.next())
				emails.add(rs.getString(1));
			sm.setResponse(ServletManager.Code.Success, emails.toString());
			sm.setLogResponse("Get testing emails");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		try {
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You ain't admin dude");
			if (!ServletFileUpload.isMultipartContent(request))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong files.");
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

			// creates the directory if it does not exist

			// parses the request's content to extract file data
			List<FileItem> formItems = upload.parseRequest(request);
			List<String> GMailEmails = new LinkedList<String>();
			List<String> GMailPasswords = new LinkedList<String>();
			int length = 0;
			String email;
			List<String> emails = new LinkedList<String>();
			if (formItems != null && formItems.size() > 0) {
				// iterates over form's fields
				for (FileItem item : formItems) {
					if (item.isFormField()) {
						if (item.getFieldName().equals("email")) {
							if (!(item.getString() == null || item.getString().equals("")))
								GMailEmails.add(item.getString());
						}
							
						else if (item.getFieldName().equals("password")) {
							if (!(item.getString() == null || item.getString().equals("")))
								GMailPasswords.add(item.getString());
						}
						else
							throw new GeneralException(ServletManager.Code.ClientError, "Unkown field");
						
					}
					
					// processes only fields that are not form fields
					else {
						String fileName = item.getName();
						if (!fileName.endsWith(".csv"))
							throw new GeneralException(ServletManager.Code.ClientError, "I need a csv file");
						String line;
						String[] lines = item.getString().split(System.getProperty("line.separator"));
						length = lines.length;
						for (int i=0; i < length; i++) {
							line = lines[i];
							if (line.startsWith("\""))
								email = line.substring(1, line.length() - 1);
							else
								email = line;
							emails.add(email);
						}
					}
				}
				if (GMailEmails.size() != GMailPasswords.size())
					throw new GeneralException(ServletManager.Code.ClientError, "Missing emails or passwords");
				if (length > GMailEmails.size() * 100)
					throw new GeneralException(ServletManager.Code.ClientError, "Too much adresses");
				if (length == 0)
					throw new GeneralException(ServletManager.Code.ClientError, "No emails");
				sm.getDB().prepareRequest("DELETE FROM testingEmails;").set();
				Thread t = new Thread(new GrowthHackingSender(GMailEmails, GMailPasswords, emails,length));
				t.start();
				sm.setRedirectUrl("admin.jsp?verifyEmails=true");
			}
		} catch(GeneralException e) {
			sm.setResponse(e);
		} catch(Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
