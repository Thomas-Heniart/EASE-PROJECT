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
				sendEmail(verificationCode, newEmail);
				SI.setResponse(200, "Email successfully added");
			} catch (MessagingException e) {
				SI.setResponse(ServletItem.Code.EMailNotSended, "Email not sent");
				e.printStackTrace();
			}
		}
		SI.sendResponse();
	}

	public void sendEmail(String verificationCode, String newEmail)
			throws UnsupportedEncodingException, MessagingException {
		// String link = "https://ease.space/AddEmail?email=" + newEmail +
		// "&code=" + verificationCode;
		String link = "http://localhost:8080/HelloWorld/AddEmail?email=" + newEmail + "&code=" + verificationCode;
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
		message.setContent("<div style='color:black;'><p>Hello !</p>"
				+ "<p>Pour valider ton email et recevoir les updates, clique sur le lien suivant: <a href='" + link
				+ "'>Valider mon email</a></p>" + "<p>A bientôt sur Ease!</p>" + "<p>La team Ease</p></div>",
				"text/html;charset=utf-8");
		Transport.send(message);
	}

}
