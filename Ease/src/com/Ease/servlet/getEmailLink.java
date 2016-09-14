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
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;

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
		RequestDispatcher rd = request.getRequestDispatcher("checkForInvitation.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String			email = request.getParameter("email");
		
		String			retMsg;
		String			invitationCode = null;
		Properties props = new Properties();
		
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if (email == null || Regex.isEmail(email) == false){
				 retMsg = "error: Bad email";
			} else {		
				ResultSet rs;
				if ((rs = db.get("select * from invitations where email ='" + email + "';")) == null || !(rs.next())){
					retMsg = "error: Seems that you are not on the list.";
					response.getWriter().print(retMsg);
					return;
				}
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
								return new PasswordAuthentication("sergii@ease-app.co","xaYsgG4-");
							}
						});
				MimeMessage message = new MimeMessage(msession);
				message.setFrom(new InternetAddress("sergii@ease-app.co", "Ease Team"));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(email));
				message.setSubject("Créez votre compte ease");
				String link = "https://ease.space/registerInv?email=" + email + "&code=" + invitationCode;
				message.setContent("<h2>Bienvenue sur version beta EASE.space !</h2>"
						+ "<div style='color:black;'><p style='margin: 0px;'>Tu vas devoir créer ton mot de passe Ease, ça sera le seul mot de passe à retenir de ta vie ! Il va permettre de crypter (et du coup sécuriser) l’ensemble de ta plateforme.</p>"
						+ "<p style='margin: 0px;'>Tu es la seule personne à le posséder et il n’est pas ré-initialisable, s’il est perdu, tu n’auras plus accès à Ease. </p>"
						+ "<p style='margin: 0px;'>Pour créer votre compte, cela se passe par => <a href='"+link+"'>ici</a></p>"
						+ "<p>Pour l’instant Ease fonctionne uniquement sur Chrome, je t’invite à le télécharger <a href='https://www.google.fr/intl/fr/chrome/browser/desktop/index.html' target='_blank'>ici</a> si tu ne l’as pas encore sur ton ordinateur!</p>"
						+ "<p>À bientôt !</p>"	
						+ "<p>La team Ease</p></div>"
						, "text/html;charset=utf-8");

				Transport.send(message);
				retMsg = "succes";
				System.out.println("Done");
			}
		} catch (SQLException e) {
			retMsg = "error: Impossible to access data base.";
		}catch (MessagingException e) {
			retMsg = "error: error when sending mail.";
		}
		response.getWriter().print(retMsg);
	}
}