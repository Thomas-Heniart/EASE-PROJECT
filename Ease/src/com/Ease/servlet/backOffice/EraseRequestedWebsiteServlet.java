package com.Ease.servlet.backOffice;

import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;


/**
 * Servlet implementation class AskInfo
 */

public class EraseRequestedWebsiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EraseRequestedWebsiteServlet() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.EraseRequestedWebsiteServlet, request, response, user);
		
		// Get Parameters
		String website = SI.getServletParam("toErase");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (!user.isAdmin(session.getServletContext())) {
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission");
		} else if (db.connect() != 0){
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
		} else {
			db.set("DELETE FROM askForSite WHERE site='" + website + "';");
			SI.setResponse(200, "Good");
		}
		SI.sendResponse();
	}
}