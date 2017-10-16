package com.Ease.Servlet.App;

import com.Ease.Catalog.Catalog;
import com.Ease.Catalog.Website;
import com.Ease.Dashboard.App.App;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
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
import java.util.Map;

/**
 * Servlet implementation class AddClassicApp
 */
@WebServlet("/AddClassicApp")
public class AddClassicApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddClassicApp() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		try {
			sm.needToBeConnected();
			String name = sm.getServletParam("name", true);
			String websiteIdsParam = sm.getServletParam("websiteIds", true);
			String profileId = sm.getServletParam("profileId", true);
			
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty name.");
			if (websiteIdsParam == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty websites");
			
			/*else if (password == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty password.");*/
			
			//Mettre un param keyDate dans le post si besoin de decrypter en RSA. Correspond à la private key RSA, 
			/*String keyDate = sm.getServletParam("keyDate", true);
			if (keyDate != null && !keyDate.equals("")) {
				password = RSA.Decrypt(password, Integer.parseInt(keyDate));
			}*/
			
			JSONParser parser = new JSONParser();
			JSONArray websiteIds = null;
			websiteIds = (JSONArray) parser.parse(StringEscapeUtils.unescapeHtml4(websiteIdsParam));
			
			//--------
			try {

				JSONArray res = new JSONArray();
				Profile profile = user.getDashboardManager().getProfile(Integer.parseInt(profileId));
				for(Object websiteId : websiteIds) {
					Website site = catalog.getWebsiteWithId(Integer.parseInt((String)websiteId));
					if (site == null)
						throw new GeneralException(ServletManager.Code.ClientError, "This website does not exist");
					Map<String, String> infos = site.getNeededInfos(sm);
					App newApp = ClassicApp.createClassicApp(profile, profile.getApps().size(), (res.size() == 0) ? name : site.getName(), site, infos, sm, user);
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
