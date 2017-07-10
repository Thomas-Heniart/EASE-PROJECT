 package com.Ease.Servlet.Tuto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.WebsiteApp;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.Utils.Crypto.RSA;

/**
 * Servlet implementation class TutoAddApps
 */
@WebServlet("/TutoAddApps")
public class TutoAddApps extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TutoAddApps() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
			String scrappedAppsString = sm.getServletParam("scrapjson", false);
			JSONParser parser = new JSONParser();
			Object array = parser.parse(StringEscapeUtils.unescapeHtml4(scrappedAppsString));
			JSONArray jsonArray = (JSONArray) array;
			int i = 0;
			int j = 0;
			while (i < jsonArray.size()) {
				j = i + 1;
				while (j < jsonArray.size()) {
					JSONObject obj1 = (JSONObject)jsonArray.get(i);
					JSONObject obj2 = (JSONObject)jsonArray.get(j);
					if (obj1.containsKey("password") && obj2.containsKey("password")) {
						if (obj1.get("websiteId").equals(obj2.get("websiteId")) && obj1.get("login").equals(obj2.get("login"))) {
							jsonArray.remove(i);
							j = jsonArray.size();
							i--;
						}
					}
					j++;
				}
				i++;
			}
			
			i = 0;
			Profile profile = null;
			App logwith = null;
			Website site;
			while (i < jsonArray.size()) {
				JSONObject obj = (JSONObject)jsonArray.get(i);
				if (i < 10) {
					if (obj.get("profileId") == null) {
						profile = user.getDashboardManager().getProfilesList().get(0);
					} else {
						profile = user.getDashboardManager().getProfile(Integer.parseInt(obj.get("profileId").toString()));
					}
				}
				site = ((Catalog)sm.getContextAttr("catalog")).getWebsiteWithId(Integer.parseInt(obj.get("websiteId").toString()));
				if (i % 10 == 0 && i != 0) {
					profile = user.getDashboardManager().addProfile("Choose name", "#ff9d34", sm.getDB());
				}
				if (!obj.containsKey("password")) {
					profile.addLogwithApp(obj.get("name").toString(), site, logwith, sm);
				} else {
					Map<String, String> infos = new HashMap<String, String>();
					String password;
					infos.put("login", obj.get("login").toString());
					if (obj.get("keyDate") != null && !obj.get("keyDate").toString().equals("")) {
						password = RSA.Decrypt(obj.get("password").toString(), Integer.parseInt(obj.get("keyDate").toString()));
					} else {
						password = obj.get("password").toString();
					}
					infos.put("password", user.encrypt(password));
					boolean ret = false;
					for (Profile p : user.getDashboardManager().getProfilesList()) {
						for (App app : p.getApps()) {
							if (app.getClass().getName().equals("WebsiteApp") && ((WebsiteApp)app).getSite() == site) {
								ClassicApp.createFromWebsiteApp((WebsiteApp)app, obj.get("name").toString(), infos, sm, user);
								ret = true;
							}
						}
					}
					if (ret == false) {
						if (obj.get("password").toString().equals("")) {
							profile.addEmptyApp(obj.get("name").toString(), site, sm);
						} else {
							logwith = profile.addClassicApp(obj.get("name").toString(), site, infos, sm);
						}
					}
				}
				i++;
			}
			sm.setResponse(ServletManager.Code.Success, "Scrapping done, app added.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
