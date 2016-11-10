package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;

import come.Ease.mail.MailThread;

/**
 * Servlet implementation class SendEmails
 */
@WebServlet("/SendEmails")
public class SendEmails extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendEmails() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("sendEmails.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.socketFactory.port", "587");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		Session msession = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
		InternetAddress from = new InternetAddress(email, "IESEG Communication");
		ResultSet rs = db.get("SELECT email, invitationCode FROM invitations WHERE email LIKE '%ieseg.fr';");
		try {
			while (rs.next()) {
				String toEmail = rs.getString(1);
				String invitationCode = rs.getString(2);
				MailThread newThread = new MailThread(("Email to" + toEmail + "."), from, toEmail, invitationCode, msession);
				newThread.start();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("All emails have been sent");
		RequestDispatcher rd = request.getRequestDispatcher("sendEmails.jsp");
		rd.forward(request, response);
	} 
}
