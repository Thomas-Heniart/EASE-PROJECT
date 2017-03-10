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
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You are not admin");
			DatabaseResult rs = db.prepareRequest("SELECT * FROM requestedWebsites;").get();
			JSONArray res = new JSONArray();
			while(rs.next()) {
				DatabaseRequest db_request = db.prepareRequest("SELECT firstName, email FROM users WHERE id= ?;");
				db_request.setInt(rs.getString("user_id"));
				DatabaseResult rs2 = db_request.get();
				rs2.next();
				String name = rs2.getString(1);
				String email = rs2.getString(2);
				String site = rs.getString("site");
				JSONObject tmpObject = new JSONObject();
				tmpObject.put("site", site);
				tmpObject.put("userName", name);
				tmpObject.put("email", email);
				tmpObject.put("date", rs.getString("date"));
				boolean alreadyIntegrated = catalog.getWebsiteWithHost(site) != null;
				tmpObject.put("alreadyIntegrated", alreadyIntegrated);
				res.add(tmpObject);
			}
			sm.setResponse(ServletManager.Code.Success, res.toString());
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
			db.set("INSERT INTO requestedWebsites values (null, " + user.getDBid() + ", '" + site + "', default);");
			sm.setResponse(ServletManager.Code.Success, "Request sent");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
