package com.Ease.Servlet.App;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.LogwithApp.LogwithApp;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class AddLogwithApp
 */
@WebServlet("/AddLogwithApp")
public class AddLogwithApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddLogwithApp() {
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
		System.out.println("Add logwith app");
		try {
			sm.needToBeConnected();
			String name = sm.getServletParam("name", true);
			String websiteIdsParam = sm.getServletParam("websiteIds", true);
			String profileId = sm.getServletParam("profileId", true);
			String logwithId = sm.getServletParam("logwithId", true);
			Website site = null;
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
			JSONParser parser = new JSONParser();
			JSONArray websiteIds = null;
			websiteIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(websiteIdsParam));
			try {
				JSONArray res = new JSONArray();
				Profile profile = user.getDashboardManager().getProfile(Integer.parseInt(profileId));
				WebsiteApp logwith = (WebsiteApp) user.getDashboardManager().getAppWithId(Integer.parseInt(logwithId));
				for(Object websiteId : websiteIds) {
					site = ((Catalog)sm.getContextAttr("catalog")).getWebsiteWithId(Integer.parseInt((String)websiteId));
					App newApp = LogwithApp.createLogwithApp(profile, profile.getApps().size(), (res.size() == 0) ? name : site.getName(), site, logwith, sm);
					profile.addApp(newApp);
					res.add(newApp.getDBid());
				}
				sm.setResponse(ServletManager.Code.Success, res.toString());
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
