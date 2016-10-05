package com.Ease.servlet;

import java.io.IOException;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

public class getEmailLink extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public getEmailLink() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession();

		User user = (User) session.getAttribute("User");
		if(user == null) {
			RequestDispatcher rd = request.getRequestDispatcher("checkForInvitation.jsp");
			rd.forward(request, response);
		}
		else {
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.GetMailLink, request, response, user);
		
		// Get Parameters
		String email = SI.getServletParam("email");
		// --
		
		String invitationCode = null;
		Properties props = new Properties();
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (email == null || Regex.isEmail(email) == false){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad email.");
			} else {		
				ResultSet rs;
				rs = db.get("select * from users where email ='" + email + "';");
				if (rs.next()) {
					SI.setResponse(ServletItem.Code.BadParameters, "You have already an account.");
				} else {
					if ((rs = db.get("select * from invitations where email ='" + email + "';")) == null || !(rs.next())){
						SI.setResponse(ServletItem.Code.BadParameters, "Sorry, you are not on the list. Please try with your school email!");
					} else {
						invitationCode = rs.getString(2);
						props.put("mail.smtp.host", "smtp.gmail.com");
						props.put("mail.smtp.socketFactory.port", "465");
						props.put("mail.smtp.socketFactory.class",
								"javax.net.ssl.SSLSocketFactory");
						props.put("mail.smtp.auth", "true");
						props.put("mail.smtp.port", "465");
						Session msession = Session.getDefaultInstance(props,
								new javax.mail.Authenticator() {
									protected PasswordAuthentication getPasswordAuthentication() {
										return new PasswordAuthentication("benjamin@ease-app.co","bpease.P2211");
									}
								});
						MimeMessage message = new MimeMessage(msession);
						message.setFrom(new InternetAddress("benjamin@ease-app.co", "Ease Team"));
						message.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(email));
						message.setSubject(MimeUtility.encodeText("Active ton compte Ease !", "utf-8", null));
						String link = "https://ease.space/registerInv?email=" + email + "&code=" + invitationCode;
						message.setContent("<div style='color:black;'><p>Hello !</p>"
								+ "<p>Bienvenue sur Ease.space !</p>"
								+ "<p>Ease est ton espace personnel intelligent et sécurisé qui regroupe l'ensemble des sites sur lesquels tu as un compte, et qui t’y connecte automatiquement!</br>Ease fonctionne sur Chrome et Safari.</p>"
								+ "<p>Autrement dit, une fois tes identifiants enregistrés sur ton espace Ease, tu n’auras plus jamais à les utiliser. Et ce, où que tu sois, quelque soit l’ordinateur et en toute sécurité. Seul pré-requis: avoir internet ! ;-)</p>"
								+ "<p>Pour activer ton espace, clique sur le lien suivant et laisse toi guider: <a href='"+link+"'>https://ease.space/...</a></p>"
								+ "<p>Avant de nous quitter, quelques petits conseils sur ton mot de passe de ton espace Ease: </p>"
								+ "<p style='margin-left: 25px;'>- Ne choisis pas le nom de ton chien, AZERTY ou 123456: un mot de passe compliqué assure une meilleure sécurité de tes données personnelles.</p>"
								+ "<p style='margin-left: 25px;'>- N’oublie pas ton mot de passe car, pour garantir la sécurité de tes données personnelles, il n’est pas ré-initialisable.</p>"
								+ "<p>A bientôt sur Ease!</p>"
								+ "<p>La team Ease</p></div>"
								, "text/html;charset=utf-8");

						Transport.send(message);
						SI.setResponse(200, "Please, go check your email.");
					}
				}
			}
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}catch (MessagingException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}