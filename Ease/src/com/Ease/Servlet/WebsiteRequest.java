package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class WebsiteRequest
 */
@WebServlet("/WebsiteRequest")
public class WebsiteRequest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebsiteRequest() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You are not admin");
			DatabaseResult rs = db.prepareRequest("SELECT id, user_id, site, DATE(date) AS date FROM requestedWebsites;").get();
			JSONArray res = new JSONArray();
			while(rs.next()) {
				DatabaseRequest db_request = db.prepareRequest("SELECT firstName, email FROM users WHERE id= ?;");
				db_request.setInt(rs.getString("user_id"));
				DatabaseResult rs2 = db_request.get();
				if (!rs2.next())
					continue;
				String name = rs2.getString(1);
				String email = rs2.getString(2);
				String site = rs.getString("site");
				JSONObject tmpObject = new JSONObject();
				tmpObject.put("db_id", rs.getString(1));
				tmpObject.put("url", site);
				tmpObject.put("userName", name);
				tmpObject.put("userEmail", email);
				tmpObject.put("date", rs.getString("date"));
				tmpObject.put("alreadyIntegrated", catalog.matchUrl(site));
				res.add(tmpObject);
			}
			sm.setResponse(ServletManager.Code.Success, res.toString());
			sm.setLogResponse("WebsiteRequest get done");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			String site = sm.getServletParam("ask", true);
			DatabaseRequest db_request = db.prepareRequest("INSERT INTO requestedWebsites values (null, ?, ?, default);");
			db_request.setInt(user.getDBid());
			db_request.setString(site);
			db_request.set();
			sm.setResponse(ServletManager.Code.Success, "Request sent");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
