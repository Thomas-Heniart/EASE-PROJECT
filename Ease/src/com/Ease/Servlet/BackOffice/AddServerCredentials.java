package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.ServerKey;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

import javax.servlet.annotation.WebServlet;


/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addServerCredentials")
public class AddServerCredentials extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddServerCredentials() {
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
		ServerKey serverKey = (ServerKey)sm.getContextAttr("serverKey");
		
		// Get Parameters
		String newLogin = sm.getServletParam("newLogin", true);
		String newPassword = sm.getServletParam("newPassword", false);
		String login = sm.getServletParam("login", true);
		String password = sm.getServletParam("password", true);
		
		DataBaseConnection db = sm.getDB();

		try {
			sm.needToBeConnected();
			if (!user.isAdmin()) {
				sm.setResponse(ServletManager.Code.ClientWarning, "You are not admin.");
			} else {
				ServerKey.createServerKey(newLogin, newPassword, login, password, db);
				sm.setResponse(ServletManager.Code.Success, "Success");
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
		
	}
}