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
import com.Ease.session.LogWith;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditLogWith
 */

public class EditLogWith extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditLogWith() {
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
		LogWith logWith = null;

		try {
			int profileIndex = Integer.parseInt(request.getParameter("profileIndex"));
			int appIndex = Integer.parseInt(request.getParameter("appIndex"));
			int lwProfileIndex = Integer.parseInt(request.getParameter("lwProfileIndex"));
			int lwAccountIndex = Integer.parseInt(request.getParameter("lwProfileIndex"));
			DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
			String name = request.getParameter("name");
			user = (User)(session.getAttribute("User"));
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0) {
				retMsg = "error: Impossible to connect data base.";
			} else if (profileIndex < 0 || profileIndex >= user.getProfiles().size()) {
				retMsg = "error: Bad profile's index.";
			} else if (appIndex < 0 || appIndex >= user.getProfiles().get(profileIndex).getApps().size()) {
				retMsg = "error: Bad website's index.";
			} else if (name == null || name.length() > 14) {
				retMsg = "error: Incorrect name";
			}  else if (lwProfileIndex < 0 || lwProfileIndex >= user.getProfiles().size()) {
				retMsg = "error: Bad lwProfileIndex.";
			} else if (lwAccountIndex < 0 || lwAccountIndex >= user.getProfiles().get(lwProfileIndex).getApps().size()){
				retMsg = "error: Bad lwAccountIndex.";
			} else if (user.getProfiles().get(lwProfileIndex).getApps().get(lwAccountIndex).getType().equals("Account") == false){
				retMsg = "error: This account is not an account.";
			} else {
				App app = user.getProfiles().get(profileIndex).getApps().get(appIndex);
				if (app.getType().equals("LogWith") == false){
					Site site = app.getSite();
					app.deleteFromDB(session.getServletContext());
					user.getProfiles().get(profileIndex).getApps().remove(appIndex);
					user.getProfiles().get(profileIndex).addApp(new LogWith(name, user.getProfiles().get(lwProfileIndex).getApps().get(lwAccountIndex).getId(), site, user.getProfiles().get(profileIndex), user.getUserKey(), session.getServletContext()));
					retMsg = "succes";
				} else {
					logWith = (LogWith)app;
					logWith.setAccountId(user.getProfiles().get(lwProfileIndex).getApps().get(lwAccountIndex).getId());
					logWith.setName(name);
					logWith.updateInDB(session.getServletContext(), user.getUserKey());
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
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditApp, retMsg + " : " + ((logWith == null) ? "null" : logWith.getSite().getName()));
		response.getWriter().print(retMsg);
	}

}
