package com.Ease.servlet.backOffice;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.AdminMessage;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/updateAdminMessage")
public class UpdateAdminName extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateAdminName() {
		super();
		// TODO Auto-generated constructor stub
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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.UpdateAdminMessage, request, response, user);
		
		// Get Parameters
		String visibleString = SI.getServletParam("visible");
		String color = SI.getServletParam("color");
		String message = SI.getServletParam("message");
		// --
		
		boolean visible;
		if(visibleString != null)
			visible = true;
		else 
			visible = false;

		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (!user.isAdmin(session.getServletContext())) {
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else {
			AdminMessage adminMessage = ((AdminMessage)session.getServletContext().getAttribute("AdminMessage"));
			adminMessage.setColor(color);
			adminMessage.setMessage(message);
			adminMessage.setVisibility(visible);
			SI.setResponse(200, "Admin message set up.");
		}
		this.doGet(request, response);
	}
}
