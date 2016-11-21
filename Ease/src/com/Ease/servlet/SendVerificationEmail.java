package com.Ease.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Mail;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class SendVerificationEmail
 */
@WebServlet("/SendVerificationEmail")
public class SendVerificationEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SendVerificationEmail() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
		ServletItem SI = new ServletItem(ServletItem.Type.SendVerificationEmail, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		
		String email = SI.getServletParam("email");
		
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (email == null || email.isEmpty()) {
			SI.setResponse(ServletItem.Code.BadParameters, "Incorrect email");
		} else if (user.getEmails().containsKey(email) == false) {
			SI.setResponse(ServletItem.Code.LogicError, "Email already verified.");
		} else {
			String verificationCode = "";
			try {
				ResultSet rs = db.get("SELECT verificationCode FROM usersEmails WHERE email = '" + email + "' AND user_id = " + user.getId() + ";");
				if (rs.next()) {
					String code = rs.getString(1);
					if (code == null) {
						String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
						Random r = new Random();
						for (int i = 0; i < 126; ++i)
							verificationCode += alphabet.charAt(r.nextInt(alphabet.length()));
						db.set("UPDATE usersEmails SET verificationCode = '" + verificationCode + "' WHERE user_id = " + user.getId() + " AND email = '" + email + "';");
					}
					else
						verificationCode = code;
				} else {
					String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
					Random r = new Random();
					for (int i = 0; i < 126; ++i)
						verificationCode += alphabet.charAt(r.nextInt(alphabet.length()));
					db.set("UPDATE usersEmails SET verificationCode = '" + verificationCode + "' WHERE user_id = " + user.getId() + " AND email = '" + email + "';");
				}
				sendEmail(verificationCode, email, user.getEmail());
			} catch (SQLException | MessagingException e) {
				e.printStackTrace();
				SI.setResponse(ServletItem.Code.LogicError, "Error");
			}
			SI.setResponse(200, "Email sent");
		}
		SI.sendResponse();
	}

	public static void sendEmail(String verificationCode, String newEmail, String askingEmail)
			throws UnsupportedEncodingException, MessagingException {
		//String link = "https://localhost:8080/HelloWorld/AddEmail?email=" + newEmail + "&code=" + verificationCode;
		String link = "https://ease.space/AddEmail?email=" + newEmail + "&code=" + verificationCode;
		Mail newMail = new Mail();
		newMail.sendVerificationEmail(newEmail, askingEmail, link);
	}

}
