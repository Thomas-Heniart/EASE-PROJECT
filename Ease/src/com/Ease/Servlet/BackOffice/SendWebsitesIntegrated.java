package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

public class SendWebsitesIntegrated {
	
	public SendWebsitesIntegrated() {
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		JSONParser parser = new JSONParser();
		try {
			String email = sm.getServletParam("email", true);
			String websitesUrlsString = sm.getServletParam("websites", true);
			if (email == null || email.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty email");
			if (websitesUrlsString == null || websitesUrlsString.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty websites");
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			ResultSet rs = db.get("SELECT firstName FROM users WHERE email = '" + email + "'");
			try {
				if (rs.next()) {
					String firstName = rs.getString(1);
					JSONArray websitesUrls = new JSONArray();
					List<Website> integratedWebsites = new LinkedList<Website>();
					try {
						websitesUrls = (JSONArray)parser.parse(websitesUrlsString);
					} catch (ParseException e) {
						throw new GeneralException(ServletManager.Code.ClientError, e);
					}
					if (websitesUrls.isEmpty())
						throw new GeneralException(ServletManager.Code.ClientError, "Empty websites");
					for (Object websiteUrl : websitesUrls) {
						integratedWebsites.add(catalog.getWebsiteWithHost((String)websiteUrl));
					}
					SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
					mail.sendAppsArrivedEmail(firstName, email, integratedWebsites);
				} else
					throw new GeneralException(ServletManager.Code.ClientError, "This user does not exist");
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
			sm.setResponse(ServletManager.Code.Success, "");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}
