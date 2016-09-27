package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.session.App;
import com.Ease.session.ClassicAccount;
import com.Ease.session.LogWithAccount;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditApp
 */

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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost2(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		App app = null;
		boolean transaction = false;
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			int appId = Integer.parseInt(request.getParameter("appId"));
			String login = request.getParameter("login");
			String wPassword = request.getParameter("wPassword");
			String name = request.getParameter("name");
			
			user = (User)(session.getAttribute("User"));
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if ((login == null || login.equals("")) && (wPassword == null || wPassword.equals(""))) {
				retMsg = "error: Bad login or password.";
			} else if (name == null || name.length() > 14) {
				retMsg = "error: Incorrect name";
			}else if (db.connect() != 0) {
				retMsg = "error: Impossible to connect data base.";
			} else if (user.getApp(appId) == null) {
				retMsg = "error: Bad appId.";
			} else {
				app = user.getApp(appId);
				if (app.havePerm(App.AppPerm.MODIFY, session.getServletContext())){
					transaction = db.start();
					if (app.getType().equals("ClassicAccount") == false){
						if (login == null || login.equals("") || wPassword == null || wPassword.equals("")) {
							retMsg = "error: Bad login or password.";
						} else {
							Site site = app.getSite();
							app.deleteFromDB(session.getServletContext());
							user.getProfiles().get(app.getProfileIndex()).getApps().remove(app);
							App tmp = new App(name, login, wPassword, site, user.getProfiles().get(app.getProfileIndex()), user, session.getServletContext());
							user.getProfiles().get(app.getProfileIndex()).addApp(tmp);
							user.getApps().remove(user.getApp(appId));
							user.getApps().add(tmp);
							tmp.setAppId(appId);
							retMsg = "succes";
						}
					} else {
						ClassicAccount account = (ClassicAccount)app.getAccount();
						if (login != null && !login.equals(""))
							account.setLogin(login);
						if (wPassword != null && !wPassword.equals(""))
							account.setPassword(wPassword);
						account.updateInDB(session.getServletContext(), user.getUserKey());
						app.setName(name);
						app.setAccount(account);
						app.updateInDB(session.getServletContext());
						retMsg = "success";
					}
					db.commit(transaction);
				} else {
					retMsg = "error: You have not the permission";
				}
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			retMsg = "error :" + e.getMsg();				
		} catch (NumberFormatException e) {
			db.cancel(transaction);
			retMsg = "error: Bad index";
		} catch (IndexOutOfBoundsException e){
			db.cancel(transaction);
			retMsg = "error: Bad app index.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, retMsg + " : " + ((app == null) ? "null" : app.getSite().getName()));
		response.getWriter().print(retMsg);
	}
	
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		App app = null;
		boolean transaction = false;
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			int appId = Integer.parseInt(request.getParameter("appId"));
			String login = request.getParameter("login");
			String wPassword = request.getParameter("wPassword");
			String name = request.getParameter("name");
			
			user = (User)(session.getAttribute("User"));
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0) {
				retMsg = "error: Impossible to connect data base.";
			} else {
				int lwId = Integer.parseInt(request.getParameter("lwId"));
				if (lwId == 0) {
					if ((login == null || login.equals("")) && (wPassword == null || wPassword.equals(""))) {
						retMsg = "error: Bad login or password.";
					} else if (name == null || name.length() > 14) {
						retMsg = "error: Incorrect name";
					}  else if (user.getApp(appId) == null) {
						retMsg = "error: Bad appId.";
					} else {
						app = user.getApp(appId);
						if (app.havePerm(App.AppPerm.MODIFY, session.getServletContext())){
							transaction = db.start();
							if (app.getType().equals("LogWithAccount") == true){
								if (login == null || login.equals("") || wPassword == null || wPassword.equals("")) {
									retMsg = "error: Bad login or password.";
								} else {
									Site site = app.getSite();
									app.deleteFromDB(session.getServletContext());
									user.getProfiles().get(app.getProfileIndex()).getApps().remove(app);
									App tmp = new App(name, login, wPassword, site, user.getProfiles().get(app.getProfileIndex()), user, session.getServletContext());
									user.getProfiles().get(app.getProfileIndex()).addApp(tmp);
									user.getApps().remove(user.getApp(appId));
									user.getApps().add(tmp);
									tmp.setAppId(appId);
									retMsg = "succes";
								}
							} else if (app.getType().equals("ClassicAccount") == true){
								ClassicAccount account = (ClassicAccount)app.getAccount();
								if (login != null && !login.equals(""))
									account.setLogin(login);
								if (wPassword != null && !wPassword.equals(""))
									account.setPassword(wPassword);
								account.updateInDB(session.getServletContext(), user.getUserKey());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								retMsg = "success";
							} else {
								ClassicAccount account = new ClassicAccount(login, wPassword, user, session.getServletContext());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								retMsg = "success";
							}
							db.commit(transaction);
						} else {
							retMsg = "error: You have not the permission";
						}
					}
				} else {
					if (user.getApp(appId) == null) {
						retMsg = "error: Bad appId.";
					} else if (name == null || name.length() > 14) {
						retMsg = "error: Incorrect name";
					} else if (user.getApp(lwId) == null) {
						retMsg = "error: Bad lwId.";
					} else if (user.getProfiles().get(user.getApp(lwId).getProfileIndex()).getApps().get(user.getApp(lwId).getIndex()).getType().equals("Account") == false){
						retMsg = "error: This account is not an account.";
					} else {
						app = user.getProfiles().get(user.getApp(appId).getProfileIndex()).getApps().get(user.getApp(appId).getIndex());
						if (app.havePerm(App.AppPerm.MODIFY, session.getServletContext())){
							transaction = db.start();
							if (app.getType().equals("ClassicAccount") == true){
								Site site = app.getSite();
								app.deleteFromDB(session.getServletContext());
								user.getProfiles().get(user.getApp(appId).getProfileIndex()).getApps().remove(user.getApp(appId));
								App tmp = new App(name, user.getApp(lwId).getId(), site, user.getProfiles().get(user.getApp(appId).getProfileIndex()), user, session.getServletContext());
								user.getProfiles().get(user.getApp(appId).getProfileIndex()).addApp(tmp);
								user.getApps().remove(user.getApp(appId));
								user.getApps().add(tmp);
								tmp.setAppId(appId);
								retMsg = "succes";
							} else if (app.getType().equals("LogWithAccount") == true) {
								LogWithAccount logWith = (LogWithAccount)app.getAccount();
								logWith.setLogWithAppId(user.getApp(lwId).getId());
								app.setName(name);
								logWith.updateInDB(session.getServletContext());
								app.setAccount(logWith);
								app.updateInDB(session.getServletContext());
								retMsg = "success";
							} else {
								LogWithAccount account = new LogWithAccount(user.getApp(lwId).getId(), session.getServletContext());
								app.setName(name);
								app.setAccount(account);
								app.updateInDB(session.getServletContext());
								retMsg = "success";
							}
							db.commit(transaction);
						} else {
							retMsg = "error: You have not the permission";
						}
					}
				}
			}
			 
		} catch (SessionException e) {
			db.cancel(transaction);
			retMsg = "error :" + e.getMsg();				
		} catch (NumberFormatException e) {
			db.cancel(transaction);
			retMsg = "error: Bad index";
		} catch (IndexOutOfBoundsException e){
			db.cancel(transaction);
			retMsg = "error: Bad app index.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, retMsg + " : " + ((app == null) ? "null" : app.getSite().getName()));
		response.getWriter().print(retMsg);
	}
}
