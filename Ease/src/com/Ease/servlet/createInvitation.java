package com.Ease.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.Regex;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.session.User.UserData;

public class createInvitation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public createInvitation() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Sending invitation ...");
		String			email = request.getParameter("email");

		String			retMsg;
		String			alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
		String			invitationCode = "";
		Properties props = new Properties();
		Connection 		con;
		ResultSet		rs;
		Random r = new Random();

		HttpSession session = request.getSession();
		
		User user = (User)session.getAttribute("User");
		if(!user.isAdmin(session.getServletContext())){
			response.getWriter().print("error: You aint admin bro");
		}
		
		try {
			if (email == null || Regex.isEmail(email) == false){
				retMsg = "error: Bad email";
			}else {	Class.forName("com.mysql.jdbc.Driver");

			for (int i = 0;i < 126 ; ++i) {
				invitationCode += alphabet.charAt(r.nextInt(alphabet.length()));			
			}
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://ease.space:3306/test", "admin", "P6au23q7");
			con.createStatement().executeUpdate("insert into invitations values ('" + email + "', '" + invitationCode + "');");

			con.close();
			
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.socketFactory.port", "465");
			props.put("mail.smtp.socketFactory.class",
					"javax.net.ssl.SSLSocketFactory");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.port", "465");
			Session msession = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("sergii@ease-app.co","xaYsgG4-");
				}
			});
			MimeMessage message = new MimeMessage(msession);
			message.setFrom(new InternetAddress("sergii@ease-app.co", "Ease Team"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject(MimeUtility.encodeText("Créez votre compte ease", "utf-8", null));
			String link = "https://ease.space/registerInv?email=" + email + "&code=" + invitationCode;
			message.setContent("<p>Hello !</p>"
					+ "<p></p>"
					+ "<p>Bienvenue sur la version beta Ease.space !</p>"
					+ "<p>Ease est une plateforme qui va te permettre de retrouver l'ensemble des sites sur lesquels tu as un compte utilisateur, afin de t'y connecter en 1 clic !</p>"
					+ "<p></p>"
					+ "<p>Tu vas bientôt créer ton mot de passe Ease et ça sera le seul mot de passe à retenir de ta vie ! Il va permettre de crypter (et du coup sécuriser) l’ensemble de ta plateforme.</p>"
					+ "<p>Tu es la seule personne à le posséder et il n’est pas ré-initialisable, s’il est perdu, tu n’auras plus accès à Ease.</p>"
					+ "<p style='margin: 0px;'>Pour activer ton compte, clique ici : <a href='"+link+"'>https://ease.space</a></p>"
					+ "<p>Ease fonctionne sur Chrome, si tu ne l'as pas, télécharge le <a href='https://www.google.fr/intl/fr/chrome/browser/desktop/index.html' target='_blank'>ici</a>. (Safari et Firefox coming soon!!)</p>"
					+ "<p></p>"
					+ "<p>À bientôt !</p>"
					+ "<p></p>"
					+ "<p>La team Ease</p>"
					, "text/html;charset=utf-8");		
			
			Transport.send(message);
			retMsg = "succes";
			System.out.println("Done");
			}
		} catch (SQLException e) {
			retMsg = "error:" + e.getMessage();
			e.printStackTrace();
		}catch (MessagingException e) {
			retMsg = "error: error when sending mail.";
		} catch (ClassNotFoundException e) {
			retMsg = "error: error when load class.";
		}
		System.out.println(retMsg);
		response.getWriter().print(retMsg);
	}
}