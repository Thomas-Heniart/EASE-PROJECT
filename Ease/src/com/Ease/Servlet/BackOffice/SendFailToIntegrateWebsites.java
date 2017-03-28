package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.Ease.Dashboard.User.User;
import com.Ease.Mail.SendGridMail;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class SendFailToIntegrateWebsites
 */
@WebServlet("/SendFailToIntegrateWebsites")
public class SendFailToIntegrateWebsites extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendFailToIntegrateWebsites() {
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		JSONParser parser = new JSONParser();
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			String websiteRequestString = sm.getServletParam("websiteRequests", true);
			if (websiteRequestString == null || websiteRequestString.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Empty website");
			JSONArray websiteRequests = new JSONArray();
			websiteRequests = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(websiteRequestString));
			if (websiteRequests.isEmpty())
				throw new GeneralException(ServletManager.Code.ClientError, "Empty websites");
			Map<Entry<String, String>, List<String>> userAndUrlsMap = new HashMap<Entry<String, String>, List<String>>();
			List<String> dbIds = new LinkedList<String>();
			for (Object websiteRequest : websiteRequests) {
				String db_id = (String) ((JSONObject) websiteRequest).get("db_id");
				dbIds.add(db_id);
				String url = (String) ((JSONObject) websiteRequest).get("url");
				JSONObject userObj = (JSONObject) ((JSONObject) websiteRequest).get("user");
				Map.Entry<String, String> entry  = new AbstractMap.SimpleEntry<String, String>((String)userObj.get("email"), (String)userObj.get("name"));
				List<String> urls = userAndUrlsMap.get(entry);
				if (urls == null) {
					urls = new LinkedList<String>();
					userAndUrlsMap.put(entry, urls);
				}
				if (!urls.contains(url))
					urls.add(url);
			}
			for (Entry<Entry<String, String>, List<String>> entry : userAndUrlsMap.entrySet()) {
				SendGridMail mail = new SendGridMail("Agathe @Ease", "contact@ease.space");
				mail.sendFailToIntegrateWebsitesEmail(entry.getKey().getValue(), entry.getKey().getKey(), entry.getValue());
			}
			DataBaseConnection db = sm.getDB();
			DatabaseRequest db_request;
			for (String requestId : dbIds) {
				db_request = db.prepareRequest("DELETE FROM requestedWebsites WHERE id = ?");
				db_request.setInt(requestId);
				db_request.set();
			}
			sm.setResponse(ServletManager.Code.Success, "Emails sent");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
