package com.Ease.Servlet.App;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class WebsiteAppToLogwithApp
 */
@WebServlet("/WebsiteAppToLogwithApp")
public class WebsiteAppToLogwithApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebsiteAppToLogwithApp() {
        super();
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
			String appIdsString = sm.getServletParam("appIds", true);
			String name = sm.getServletParam("name", true);
			String logwithId = sm.getServletParam("logwithId", true);
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
			if (appIdsString == null || appIdsString.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong app id.");
			if (logwithId == null || logwithId.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong logwith id");
			try {
				JSONParser parser = new JSONParser();
				JSONArray appIds = null;
				appIds = (JSONArray)parser.parse(appIdsString);
				App logwith = user.getDashboardManager().getAppWithID(Integer.parseInt((String)logwithId));
				if (!(logwith).getType().equals("ClassicApp"))
					throw new GeneralException(ServletManager.Code.ClientError, "This is not a classic app.");
				
				DataBaseConnection db = sm.getDB();
				int transaction = db.startTransaction();
				for (Object appId : appIds) {					
					App app = user.getDashboardManager().getAppWithID(Integer.parseInt((String)appId));
					if (!(app).getType().equals("WebsiteApp"))
						throw new GeneralException(ServletManager.Code.ClientError, "This is not an empty app.");
					LogwithApp.createFromWebsiteApp((WebsiteApp)app, name, (WebsiteApp)logwith, sm, user);
				}
				db.commitTransaction(transaction);
				sm.setResponse(ServletManager.Code.Success, "Logwith app created instead of website app.");
			} catch (NumberFormatException e) {
				sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
