package com.Ease.Servlet.Tuto;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.RSA;

/**
 * Servlet implementation class TutoStep
 */
@WebServlet("/FilterScrap")
public class FilterScrap extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterScrap() {
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
		Catalog catalog = (Catalog) (session.getAttribute("catalog"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			String scrappedAppsString = sm.getServletParam("scrapjson", false);
			JSONParser parser = new JSONParser();
			Object temp = parser.parse(scrappedAppsString);
			JSONObject scrappedAppsJson = (JSONObject) temp;
			
			JSONArray facebookApps = (JSONArray)scrappedAppsJson.get("Facebook");
			JSONArray facebookAppsToKeep = new JSONArray();
			if(facebookApps != null){
				for(Object appNoType : facebookApps){
					String app = (String) appNoType;
					String websiteSingleId;
					if((websiteSingleId = Website.existsInDbFacebook(app, sm))!=null){
						JSONObject newApp = new JSONObject();
						newApp.put("websiteId", websiteSingleId);
						int singleId = Integer.parseInt((String)websiteSingleId);
						Website website = catalog.getWebsiteWithSingleId(singleId);
						newApp.put("name",website.getName());
						newApp.put("profileId",user.getProfileColumns().get(1).get(0).getSingleId());
						facebookAppsToKeep.add(newApp);
					} else {
						user.rememberNotIntegratedFacebookApp(app);
					}
				}
			}
			
			JSONArray linkedinApps = (JSONArray)scrappedAppsJson.get("Linkedin");
			JSONArray linkedinAppsToKeep = new JSONArray();
			if(linkedinApps != null){
				for(Object appNoType : linkedinApps){
					String app = (String) appNoType;
					String websiteSingleId;
					if((websiteSingleId = Website.existsInDbLinkedin(app, sm))!=null){
						JSONObject newApp = new JSONObject();
						newApp.put("websiteId", websiteSingleId);
						int singleId = Integer.parseInt((String)websiteSingleId);
						Website website = catalog.getWebsiteWithSingleId(singleId);
						newApp.put("name",website.getName());
						newApp.put("profileId",user.getProfileColumns().get(1).get(0).getSingleId());
						linkedinAppsToKeep.add(newApp);
					} else {
						user.rememberNotIntegratedLinkedinApp(app);
					}
				}
			}

			JSONArray chromeApps = (JSONArray)scrappedAppsJson.get("Chrome");
			JSONArray chromeAppsToKeep = new JSONArray();
			if(chromeApps != null){
				for(Object appNoType : chromeApps){
					JSONObject app = (JSONObject) appNoType;
					String appName = (String) app.get("website");
					JSONArray websiteSingleIds;
					if((websiteSingleIds = Website.existsInDb(appName, sm)).size() > 0){
						for(Object websiteId : websiteSingleIds){
							JSONObject newApp = new JSONObject();
							int singleId = Integer.parseInt((String)websiteId);
							Website website = catalog.getWebsiteWithSingleId(singleId);
							newApp.put("name",website.getName());
							newApp.put("profileId",user.getProfileColumns().get(1).get(0).getSingleId());
							newApp.put("login",app.get("login"));
							newApp.put("password",app.get("pass"));
							newApp.put("websiteId",websiteId);
							newApp.put("keyDate",app.get("keyDate"));
							chromeAppsToKeep.add(newApp);
						}
					} else {
						user.rememberNotIntegratedApp(app);
					}
				}
			}
			
			JSONObject result = new JSONObject();
			result.put("Facebook", facebookAppsToKeep);
			result.put("Linkedin", linkedinAppsToKeep);
			result.put("Chrome", chromeAppsToKeep);
			String resultString = result.toString();
			sm.setLogResponse("App scrapped filtered.");
			sm.setResponse(ServletManager.Code.Success, resultString);
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			sm.setResponse(new GeneralException(ServletManager.Code.InternError, e));
		}
		sm.sendResponse();
	}

}
