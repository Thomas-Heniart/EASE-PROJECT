package com.Ease.Servlet.BackOffice;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/**
 * Servlet implementation class AddApp
 */
@WebServlet("/aspirateur")
public class Aspirateur extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Aspirateur() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		try {
			sm.needToBeConnected();
			if (!user.isAdmin()) {
				sm.setResponse(ServletManager.Code.ClientWarning, "You are not admin.");
			} else {
				db.prepareRequest("DELETE FROM savedSessions WHERE datetime < SUBTIME(CURRENT_TIMESTAMP, '2 0:0:0.0');").set();
                //catalog.refresh(sm);
                sm.setResponse(ServletManager.Code.Success,"SavedSessions cleaned and Catalog refreshed.");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();

	}
}