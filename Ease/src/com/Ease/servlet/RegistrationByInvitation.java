package com.Ease.servlet;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.session.User;


/**
 * Servlet implementation class NewUser
 */
@WebServlet(urlPatterns = {"/ieseg", "/letsgo"})
public class RegistrationByInvitation extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationByInvitation() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String invitationCode = request.getParameter("invitationCode");
		String email = request.getParameter("email");
		User user = (User) session.getAttribute("User");
		RequestDispatcher rd = null;
		String dispatch;
		if (invitationCode == null || email == null)
			dispatch = "checkForInvitation.jsp";
		else if (user != null)
			dispatch = "index.jsp";
		else
			dispatch = "registrationByInvitation.jsp";
		rd = request.getRequestDispatcher(dispatch);
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.RegistrationByInvitation, request, response, user);
		
		// Get Parameters
		String invitationCode = SI.getServletParam("invitationCode");
		String fname = SI.getServletParam("fname");
		String lname = SI.getServletParam("lname");
		String email = SI.getServletParam("email");
		// --
		
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		ResultSet rs = null;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		if (user != null) {
			SI.setResponse(ServletItem.Code.AlreadyConnected, "You are logged on Ease.");
		} else if (db.connect() != 0){
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
		} else if (fname == null || fname.length() < 2){
			SI.setResponse(ServletItem.Code.BadParameters, "Your name is too short.");	
		}  else if (email == null || Regex.isEmail(email) == false){
			SI.setResponse(ServletItem.Code.BadParameters, "Incorrect email.");
		} else if (password == null || Regex.isPassword(password) == false) {
			SI.setResponse(ServletItem.Code.BadParameters, "Password is too short (at least 8 characters).");
		} else if (confirmPassword == null || password.equals(confirmPassword) == false) {
			SI.setResponse(ServletItem.Code.BadParameters, "Passwords are not the same.");
		} else {
			try {
				String group;
				rs = db.get("select * from invitations where email ='" + email + "' and linkCode = '" + invitationCode + "';");
				if (rs.next()) {
					group = rs.getString(3);
					rs = db.get("select * from users where email = '" + email + "' limit 0, 1;");
					if (rs.next()) {
						SI.setResponse(ServletItem.Code.BadParameters, "You already have an account.");
					} else {
						lname = "";
						user = new User(fname, lname, email, "0606060606", password, session.getServletContext());
						if (group != null && group.equals("null") == false)
							db.set("insert into GroupAndUserMap values (NULL, " + group + ", " + user.getId() + ");");
						user.checkForGroup(session.getServletContext());
						db.set("delete from invitations where email = '" + email + "' and linkCode = '" + invitationCode + "';");
						session.setAttribute("User", user);
						db.set("CALL addEmail(" + user.getId() + ", '" + user.getEmail() + "');");
						db.set("UPDATE usersEmails SET verified = 1 WHERE user_id = " + user.getId() + " AND email = '" + user.getEmail() + "';");
						SessionSave sessionSave = new SessionSave(user, session.getServletContext());
						session.setAttribute("User", user);
						session.setAttribute("SessionSave", sessionSave);
						SI.setResponse(200, "Successfull registration! You will be redirect in few seconds :)");
					}
				} else {
					SI.setResponse(ServletItem.Code.BadParameters, "You have no invitation or you already have an account.");
				}
			} catch (SessionException e) {
				e.printStackTrace();
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			} catch (SQLException e) {
				e.printStackTrace();
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			}
		}
		SI.sendResponse();
	}
	
	public static void sendChallengeEmail(String email) throws UnsupportedEncodingException, MessagingException {
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
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
		message.setSubject(MimeUtility.encodeText("Mr. Ammeux got one, time to win yours !", "utf-8", null));
		message.setContent("<div style='color: black;'><p>Hello dear Ease user !<br /></p>"
				+ "<p>We would like to thank you for being one of the first 1000 users of Ease!</p>"
				+ "<p>To celebrate, we offer you a chance to win your Ease sweat-shirt (the one like JP Ammeux 😉). You must invite 3 IESEG friends to join!<br/>"
				+ "Click here to get your chance:  <a href='https://goo.gl/forms/TBTvUDfwpoelcmV22'>ease.space/...</a></p>"
				+ "<p>See you soon !</p>" + "<p>The Ease team</p>" + "</div>", "text/html;charset=utf-8");
		Transport.send(message);
	}
}