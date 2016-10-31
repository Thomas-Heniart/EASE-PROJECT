package com.Ease.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AddEmail
 */
@WebServlet("/AddEmail")
public class AddEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddEmail() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AddEmail, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		if (db.connect() != 0) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "Database not connected");
			SI.sendResponse();
		}
		String verificationCode = SI.getServletParam("code");
		String email = SI.getServletParam("email");
		db.set("UPDATE usersEmails SET verified = 1 WHERE email = '" + email + "' AND verificationCode = '" + verificationCode + "';");
		if (user != null) {
			user.validateEmail(email);
		}
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AddEmail, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
		String verificationCode = "";
		Random r = new Random();
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			SI.sendResponse();
		}
		if (db.connect() != 0) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected,
					"There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
		}

		// getParams
		String newEmail = SI.getServletParam("newEmail");
		if (newEmail != null && !newEmail.isEmpty()) {
			for (int i = 0; i < 126; ++i) {
				verificationCode += alphabet.charAt(r.nextInt(alphabet.length()));
			}
			db.set("INSERT INTO usersEmails values (null, " + user.getId() + ", '" + newEmail + "', 0, '"
					+ verificationCode + "');");
			user.addEmailIfNotPresent(newEmail);
			try {
				SendVerificationEmail.sendEmail(verificationCode, newEmail, user.getEmail());
				SI.setResponse(200, "Email successfully added");
			} catch (MessagingException e) {
				SI.setResponse(ServletItem.Code.EMailNotSended, "Email not sent");
				e.printStackTrace();
			}
		}
		SI.sendResponse();
	}

}
