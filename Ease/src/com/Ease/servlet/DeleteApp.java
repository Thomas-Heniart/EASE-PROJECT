package com.Ease.servlet;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.Ease.context.DataBase;
import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.ClassicAccount;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class DeleteApp
 */
@WebServlet("/deleteApp")
public class DeleteApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteApp() {
		super();
		// TODO Auto-generated constructor stub
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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.DeleteApp, request, response, user);
		
		// Get Parameters
		String appIdParam = SI.getServletParam("appId");
		// --
		
		String appName = "";
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		try {
			int appId = Integer.parseInt(appIdParam);
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (user.getApp(appId) == null) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad appId.");
			} else {
				App app = user.getApp(appId);
				JSONObject res = new JSONObject();
				if (app.havePerm(App.AppPerm.DELETE, session.getServletContext())){
					transaction = db.start();	
					appName = app.getSite().getName();
					String email = null;
					if (app.getAccount().getType().equals("ClassicAccount"))
						email = ((ClassicAccount)app.getAccount()).getLogin();
					app.deleteFromDB(session.getServletContext());
					user.getProfile(app.getProfileId()).getApps().remove(app);
					user.getApps().remove(app);
					db.set("CALL decreaseRatio(" + app.getSite().getId() + ");");
					if (email != null) {
						ResultSet emailRs = db.get("SELECT count(distinct usersEmails.email) FROM (((apps join profiles ON apps.profile_id = profiles.profile_id) JOIN users on profiles.user_id = users.user_id) JOIN usersEmails ON users.user_id = usersEmails.user_id) JOIN ClassicAccountsInformations ON apps.account_id = ClassicAccountsInformations.account_id AND usersEmails.email = ClassicAccountsInformations.information_value WHERE users.user_id = " + user.getId() + " AND usersEmails.email = '" + email + "' AND verified=0;");
						if (emailRs.next()) {
							int ct = Integer.parseInt(emailRs.getString(1));
							if (ct == 0) {
								user.removeEmail(email);
								db.set("DELETE FROM usersEmails WHERE user_id=" + user.getId() + " AND email='" + email + "' AND verified=0;");
								res.put("email", email);
							}
						}
					}
					db.commit(transaction);
					res.put("app", appName);
					SiteManager siteManager = (SiteManager)session.getAttribute("siteManager");
					siteManager.decreaseSiteRatio(app.getSite().getId());
					SI.setResponse(200, res.toString());
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException | SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}
}
