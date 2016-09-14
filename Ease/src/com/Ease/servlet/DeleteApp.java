package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.session.App;
//import com.Ease.data.Hashing;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class DeleteApp
 */

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
		
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		String appName = "";
		
		try {
			int appId = Integer.parseInt(request.getParameter("appId"));
			//String password = request.getParameter("password");

			DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

			user = (User)(session.getAttribute("User"));
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.DeleteApp, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if (user.getApp(appId) == null) {
				retMsg = "error: Bad appId.";
			} /*else if (password == null || !Hashing.SHA(password, user.getSaltEase()).equals(user.getPassword())) {
				response.getWriter().print("error: Bad password");
			}*/ else {
				App app = user.getApp(appId);
				appName = app.getSite().getName();
				app.deleteFromDB(session.getServletContext());
				user.getProfile(app.getProfileId()).getApps().remove(app);
				user.getApps().remove(app);
				retMsg = "success";
			}
		} catch (SessionException e) {
			e.printStackTrace();
			retMsg = "error :" + e.getMsg();
			
		} catch (NumberFormatException e) {
			retMsg = "error: Bad index";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.DeleteApp, retMsg + " : " + appName);
		response.getWriter().print(retMsg);
	}
}
