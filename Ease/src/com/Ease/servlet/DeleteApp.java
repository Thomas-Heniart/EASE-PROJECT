package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.SessionException;
import com.Ease.session.User;

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
			int appId = Integer.parseInt(appIdParam);
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (user.getApp(appId) == null) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad appId.");
			} else {
				App app = user.getApp(appId);
				if (app.havePerm(App.AppPerm.DELETE, session.getServletContext())){
					transaction = db.start();	
					appName = app.getSite().getName();
					app.deleteFromDB(session.getServletContext());
					user.getProfile(app.getProfileId()).getApps().remove(app);
					user.getApps().remove(app);
					SI.setResponse(200, appName + " deleted.");
					db.commit(transaction);
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}
}
