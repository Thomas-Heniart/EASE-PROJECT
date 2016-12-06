package com.Ease.Servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.GroupProfile;
import com.Ease.Context.Group.ProfilePermissions;
import com.Ease.Dashboard.App.ClassicApp;
import com.Ease.Dashboard.App.Website;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		String password = null;
		String name = null;
		Website site = null;
		Map<String, String> informations = new HashMap<String, String>();
		try {
			sm.needToBeConnected();
			String single_id = sm.getServletParam("single_id", true);
			Map<String, String[]> params = sm.getServletParametersMap(true);
			Profile profile = user.getProfile(Integer.parseInt(single_id));
			GroupProfile groupProfile = profile.getGroupProfile();
			if (groupProfile != null
					&& (!groupProfile.getPerms().havePermission(ProfilePermissions.Perm.ADDAPP.getValue())))
				throw new GeneralException(ServletManager.Code.ClientError,
						"You do not have permission to had an app in this profile");
			@SuppressWarnings("unchecked")
			Map<Integer, Website> sites = (Map<Integer, Website>) sm.getContextAttr("sites");
			if (single_id == null)
				throw new GeneralException(ServletManager.Code.ClientError, "single_id is null.");
			if (params == null)
				throw new GeneralException(ServletManager.Code.ClientError, "There is no parameter.");
			try {
				for (Map.Entry<String, String[]> entry : params.entrySet()) {
					switch (entry.getKey().toLowerCase()) {
					case "password":
						password = entry.getValue()[0];
						if (password == null || password.equals(""))
							throw new GeneralException(ServletManager.Code.ClientError, "Empty password.");
						break;

					case "name":
						name = entry.getValue()[0];
						if (name == null || name.equals(""))
							throw new GeneralException(ServletManager.Code.ClientError, "Empty name.");
						break;

					case "single_id":
						site = sites.get(Integer.parseInt(entry.getValue()[0]));
						if (site == null)
							throw new GeneralException(ServletManager.Code.ClientError,
									"No website for this single_id");
						break;

					default:
						informations.put(entry.getKey(), entry.getValue()[0]);
						break;
					}
				}

				ClassicApp.createClassicApp(name, profile, site, password, informations, sm);
				sm.setResponse(ServletManager.Code.Success, "App added.");
			} catch (NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
