package com.Ease.servlet.backOffice;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/cleanSavedSessions")
public class CleanSavedSessions extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CleanSavedSessions() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		ServletItem SI = new ServletItem(ServletItem.Type.CleanSavedSessions, request, response, user);

		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		if(user == null){
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if(!user.isAdmin(session.getServletContext())){
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else {
			try {
				db.set("DELETE FROM savedSessions WHERE datetime < SUBTIME(CURRENT_TIMESTAMP, '2 0:0:0.0');");	
				SiteManager siteManager = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
				siteManager.refresh(db);
				SI.setResponse(200,"SavedSessions cleaned and SiteManager refreshed.");
			} catch (SQLException e) {
				e.printStackTrace();
				SI.setResponse(ServletItem.Code.LogicError, "Sql failed");
			}
		}
		SI.sendResponse();
	}
}
