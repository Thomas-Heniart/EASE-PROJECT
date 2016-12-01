package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.data.ServletItem;
import com.Ease.session.User;


/**
 
 */
@WebServlet("/checkConnection")
public class CheckConnection extends HttpServlet {
	
	private static final long serialVersionUID = 1513926919356902787L;

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

		/*HttpSession session = request.getSession();
		String email = request.getParameter("email");
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.CheckConnection, request, response, user);
		if ((!email.equals("") && user == null) || (user != null && !email.equals(user.getEmail()))) {
			SI.setResponse(ServletItem.Code.ConnectionLost, "Wrong sessions. Reload the page.");
		} else {
			SI.setResponse(200, "");
		}
		SI.sendResponse();*/
	}
}