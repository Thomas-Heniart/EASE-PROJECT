package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.SQLException;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Mail;
import com.Ease.data.ServletItem;
import com.Ease.session.User;


/**
 * Servlet implementation class AskInfo
 */
@WebServlet("/sendWebsitesIntegrated")
public class SendRequestedWebsiteValidation extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendRequestedWebsiteValidation() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.SendRequestedWebsiteValidation, request, response, user);
		
		// Get Parameters
		String email= SI.getServletParam("email");
		String websites = SI.getServletParam("websites");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (!user.isAdmin(session.getServletContext())) {
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission");
		} else {
			try {
				String[] websitesArray = websites.split("---&---");
				Mail mail = new Mail();
				mail.sendIntegratedWebsitesMail(email, websitesArray, null);
				SI.setResponse(200, "Success");
			} catch (MessagingException e) {
				SI.setResponse(ServletItem.Code.EMailNotSended, ServletItem.getExceptionTrace(e));
			}
		}
		SI.sendResponse();
	}
}