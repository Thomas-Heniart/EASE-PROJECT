package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.ClassicAccount;
import com.Ease.session.LogWithAccount;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class EditApp
 */
@WebServlet("/editApp")
public class EditApp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditApp() {
		super();
		// TODO Auto-generated constructor stub
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.EditApp, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		// Get Parameters
		String wPassword = request.getParameter("wPassword");
		String name = SI.getServletParam("name");
		String appIdParam = SI.getServletParam("appId");
		Map<String, String> informations = new HashMap<String, String>();

		if (wPassword != null)
			informations.put("password", wPassword);
		// --
		App app = null;
		boolean transaction = false;

		try {
			int appId = Integer.parseInt(appIdParam);
			ResultSet informationsRs = db.get("SELECT information_name FROM websitesInformations WHERE website_id =" + user.getApp(appId).getSite().getId() + " AND information_name <> 'password';");
			while (informationsRs.next())
					informations.put(informationsRs.getString(1), SI.getServletParam(informationsRs.getString(1)));
			
			String login = informations.get("login");
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0) {
				SI.setResponse(ServletItem.Code.DatabaseNotConnected,
						"There is a problem with our Database, please retry in few minutes.");
			} else {
				String logWithId = SI.getServletParam("lwId");
				if (logWithId == null || logWithId.isEmpty()) {
					if ((login == null || login.equals(""))) {
						SI.setResponse(ServletItem.Code.BadParameters, "Incorrect login or password.");
					} else if (name == null || name.length() > 14) {
						SI.setResponse(ServletItem.Code.BadParameters, "Incorrect name.");
					} else if (user.getApp(appId) == null) {
						SI.setResponse(ServletItem.Code.BadParameters, "Incorrect appId.");
					} else {
						app = user.getApp(appId);
						if (app.havePerm(App.AppPerm.MODIFY, session.getServletContext())) {
							transaction = db.start();
							if (app.getType().equals("LogWithAccount") == true) {
								if (login == null || login.equals("") || wPassword==null || wPassword.equals("")) {
									SI.setResponse(ServletItem.Code.BadParameters, "Incorrect login or password.");
								} else {
									Site site = app.getSite();
									app.deleteFromDB(session.getServletContext());
									user.getProfiles().get(app.getProfileIndex()).getApps().remove(app);
									App tmp = new App(informations, name, site,
											user.getProfiles().get(app.getProfileIndex()), user,
											session.getServletContext());
									user.getProfiles().get(app.getProfileIndex()).addApp(tmp);
									user.getApps().remove(user.getApp(appId));
									user.getApps().add(tmp);
									tmp.setAppId(appId);
									SI.setResponse(200, tmp.getSite().getName() + " edited.");
								}
							} else if (app.getType().equals("ClassicAccount") == true) {
								
								ClassicAccount account = (ClassicAccount) app.getAccount();
								account.updateWithInformations(informations);
								account.updateInDB(session.getServletContext(), user.getUserKey());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								if (Regex.isEmail(login))
									db.set("CALL addEmail(" + user.getId() + ", '" +login + "');");
								SI.setResponse(200, app.getSite().getName() + " edited.");
							} else {
								ClassicAccount account = new ClassicAccount(informations, user, session.getServletContext());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								if (Regex.isEmail(login))
									db.set("CALL addEmail(" + user.getId() + ", '" + login + "');");
								SI.setResponse(200, app.getSite().getName() + " edited.");
							}
							db.commit(transaction);
						} else {
							SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
						}
					}
				} else {
					int lwId = Integer.parseInt(logWithId);
					if (user.getApp(appId) == null) {
						SI.setResponse(ServletItem.Code.BadParameters, "Bad appId.");
					} else if (name == null || name.length() > 14) {
						SI.setResponse(ServletItem.Code.BadParameters, "Bad name.");
					} else if (user.getApp(lwId) == null) {
						SI.setResponse(ServletItem.Code.BadParameters, "Bad lwId.");
					} else if (user.getApp(lwId).getType().equals("ClassicAccount") == false) {
						SI.setResponse(ServletItem.Code.LogicError, "This is not a classicAccount.");
					} else {
						app = user.getProfiles().get(user.getApp(appId).getProfileIndex()).getApps()
								.get(user.getApp(appId).getIndex());
						if (app.havePerm(App.AppPerm.MODIFY, session.getServletContext())) {
							transaction = db.start();
							if (app.getType().equals("ClassicAccount") == true) {
								Site site = app.getSite();
								app.deleteFromDB(session.getServletContext());
								user.getProfiles().get(user.getApp(appId).getProfileIndex()).getApps()
										.remove(user.getApp(appId));
								App tmp = new App(name, user.getApp(lwId).getId(), site,
										user.getProfiles().get(user.getApp(appId).getProfileIndex()), user,
										session.getServletContext());
								user.getProfiles().get(user.getApp(appId).getProfileIndex()).addApp(tmp);
								user.getApps().remove(user.getApp(appId));
								user.getApps().add(tmp);
								tmp.setAppId(appId);
								SI.setResponse(200, tmp.getSite().getName() + " edited.");
							} else if (app.getType().equals("LogWithAccount") == true) {
								LogWithAccount logWith = (LogWithAccount) app.getAccount();
								logWith.setLogWithAppId(user.getApp(lwId).getId());
								app.setName(name);
								logWith.updateInDB(session.getServletContext());
								app.setAccount(logWith);
								app.updateInDB(session.getServletContext());
								SI.setResponse(200, app.getSite().getName() + " edited.");
							} else {
								LogWithAccount account = new LogWithAccount(user.getApp(lwId).getId(),
										session.getServletContext());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								SI.setResponse(200, app.getSite().getName() + " edited.");
							}
							db.commit(transaction);
						} else {
							SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
						}
					}
				}
			}

		} catch (SessionException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			db.cancel(transaction);
			e.printStackTrace();
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		} catch (IndexOutOfBoundsException | SQLException e) {
			db.cancel(transaction);
			e.printStackTrace();
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}
