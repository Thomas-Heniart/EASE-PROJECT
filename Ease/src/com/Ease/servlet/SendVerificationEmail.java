package com.Ease.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
		
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (db.connect() != 0) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
		} else if (email == null || email.isEmpty()) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad email");
		} else {
			String verificationCode = null;
			ResultSet rs = db.get("SELECT verificationCode FROM usersEmails WHERE email = '" + email + "' AND user_id = " + user.getId() + ";");
			try {
				if (rs.next()) {
					verificationCode = rs.getString(1);
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
		String link = "https://ease.space/AddEmail?email=" + newEmail + "&code=" + verificationCode;
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		Session msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("benjamin@ease-app.co", "bpease.P2211");
			}
		});
		MimeMessage message = new MimeMessage(msession);
		message.setFrom(new InternetAddress("benjamin@ease-app.co", "Ease Team"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(newEmail));
		message.setSubject(MimeUtility.encodeText("Validation email !", "utf-8", null));
		message.setContent("<div style='color: black;'><p>Hello !<br /></p>"
				+ "<p>A validation email has been asked by " + askingEmail + ". "
				+ "To validate this new email in order to receive updates, click on the link <a href='" + link
				+ "'>here</a>.</p>"
				+ "<p>(If you have not asked for a validation on <a href='https://ease.space'>https://ease.space</a>, you can ignore this email.)</p>"
				+ "<p>See you soon !</p>" + "<p>The Ease team</p>" + "</div>", "text/html;charset=utf-8");
		Transport.send(message);
	}

}
