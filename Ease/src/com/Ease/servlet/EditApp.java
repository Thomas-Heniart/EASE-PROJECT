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
import com.Ease.session.Account;
import com.Ease.session.App;
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		Account account = null;
		
		try {
			int profileIndex = Integer.parseInt(request.getParameter("profileIndex"));
			int appIndex = Integer.parseInt(request.getParameter("appIndex"));
			String login = request.getParameter("login");
			String wPassword = request.getParameter("wPassword");
			String name = request.getParameter("name");
			DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
			
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
			} else if (profileIndex < 0 || profileIndex >= user.getProfiles().size()) {
				retMsg = "error: Bad profile's index.";
			} else if (appIndex < 0 || appIndex >= user.getProfiles().get(profileIndex).getApps().size()) {
				retMsg = "error: Bad website's index.";
			} else {
				App app = user.getProfiles().get(profileIndex).getApps().get(appIndex);
				System.out.println(app.getType());
				if (app.getType().equals("Account") == false){
					if (login == null || login.equals("") || wPassword == null || wPassword.equals("")) {
						retMsg = "error: Bad login or password.";
					} else {
						Site site = app.getSite();
						app.deleteFromDB(session.getServletContext());
						user.getProfiles().get(profileIndex).getApps().remove(appIndex);
						user.getProfiles().get(profileIndex).addApp(new Account(name, login, wPassword, site, user.getProfiles().get(profileIndex), user.getUserKey(), session.getServletContext()));
						retMsg = "succes";
					}
				} else {
					account = (Account)app;
					if (login != null && !login.equals(""))
						account.setLogin(login);
					if (wPassword != null && !wPassword.equals(""))
						account.setPassword(wPassword);
					account.setName(name);
					account.updateInDB(session.getServletContext(), user.getUserKey());
					retMsg = "success";
				}
			}
		} catch (SessionException e) {
			retMsg = "error :" + e.getMsg();				
		} catch (NumberFormatException e) {
			retMsg = "error: Bad index";
		} catch (IndexOutOfBoundsException e){
			retMsg = "error: Bad app index.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, retMsg + " : " + ((account == null) ? "null" : account.getSite().getName()));
		response.getWriter().print(retMsg);
	}
}
