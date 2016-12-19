package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class ConnectionServlet
 */
@WebServlet("/CheckConnection")
public class CheckConnection extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CheckConnection() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, false);
		User user = (User)session.getAttribute("user");
		// Get Parameters
		String email = sm.getServletParam("email", true);
		//--
		if(user != null && user.getEmail().equals(email)){
			sm.setResponse(ServletManager.Code.Success, "Still connected.");
		} else {
			sm.setResponse(ServletManager.Code.ClientWarning, "User disconnected.");
			//sm.setRedirectUrl("index.jsp");
		}
		sm.sendResponse();
	}
}