package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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

/**
 * Servlet implementation class SendWebsitesIntegrated
 */
@WebServlet("/SendWebsitesIntegrated")
public class SendWebsitesIntegrated extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendWebsitesIntegrated() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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
			ResultSet rs = db.get("SELECT id, firstName FROM users WHERE email = '" + email + "'");
			try {
				if (rs.next()) {
					String user_id = rs.getString(1);
					String firstName = rs.getString(2);
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
					for (Website website : integratedWebsites)
						db.set("INSERT INTO integrateWebsitesAndUsersMap values (null, " + website.getDb_id() + ", " + user_id + ");");
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
