package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		JSONParser parser = new JSONParser();
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			String email = sm.getServletParam("email", true);
			String name = sm.getServletParam("name", true);
			String db_id = sm.getServletParam("db_id", true);
			String website = sm.getServletParam("website", true);
			if (email == null || email.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty email");
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty name");
			if (db_id == null || db_id.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty db_id");
			if (website == null || website.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty website");
			Catalog catalog = (Catalog)sm.getContextAttr("catalog");
			
				JSONArray websitesUrls = new JSONArray();
				List<Website> integratedWebsites = new LinkedList<Website>();
				try {
					websitesUrls = (JSONArray)parser.parse(StringEscapeUtils.unescapeHtml4(websitesUrlsString));
				} catch (ParseException e) {
					throw new GeneralException(ServletManager.Code.ClientError, e);
				}
				if (websitesUrls.isEmpty())
					throw new GeneralException(ServletManager.Code.ClientError, "Empty websites");
				for (Object websiteUrl : websitesUrls) {
					Website tmp = catalog.getWebsiteWithHost((String)websiteUrl);
					if (tmp == null)
						throw new GeneralException(ServletManager.Code.ClientError, "We don't have this website");
					integratedWebsites.add(tmp);
				}
				SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
				mail.sendAppsArrivedEmail(name, email, website);
				for (Website website : integratedWebsites) {
					db_request = db.prepareRequest("INSERT INTO integrateWebsitesAndUsersMap values (null, ?, ?);");
					db_request.setInt(website.getDb_id());
					db_request.setInt(user_id);
					db_request.set();
				}
			sm.setResponse(ServletManager.Code.Success, "");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
