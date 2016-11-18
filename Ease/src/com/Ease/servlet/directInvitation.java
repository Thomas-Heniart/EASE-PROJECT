package com.Ease.servlet;

import java.io.IOException;
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
import com.Ease.data.Mail;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;

@WebServlet("/directInvitation")
public class directInvitation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public directInvitation() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		ServletItem SI = new ServletItem(ServletItem.Type.TheFamilyInvitation, request, response, null);
		
		// Get Parameters
		String	email = SI.getServletParam("email");
		if(email!=null){
			email.replaceAll(" ", "");
			email.replaceAll("\r", "");
			email.replaceAll("\n", "");
			email.replaceAll("\t", "");
		}
		// --
		
		String			alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
		String			invitationCode = "";
		Properties props = new Properties();
		ResultSet		rs;
		Random r = new Random();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}

			try {
				if (email == null || Regex.isEmail(email) == false){
					SI.setResponse(ServletItem.Code.BadParameters, "This is not an email.");
				} else {					
					rs = db.get("select * from users where email='" + email + "';");
					if (rs.next()) {
						SI.setResponse(199, "You already have an account.");
					} else {
						rs = db.get("select * from invitations where email='" + email + "';");
						if (rs.next()) {
							invitationCode = rs.getString(2);
						} else {
							for (int i = 0;i < 126 ; ++i) {
								invitationCode += alphabet.charAt(r.nextInt(alphabet.length()));			
							}
							db.set("insert into invitations values ('" + email + "', '" + invitationCode + "', NULL);");
						}
						Mail newEmail = new Mail();
						newEmail.sendTheFamilyInvitation(email, invitationCode);
						SI.setResponse(200, "Please, go check your mails at "+ email +" ;)");
					}
				}
			} catch (SQLException e) {
				SI.setResponse(ServletItem.Code.LogicError, "SQL Exception");
			} catch (MessagingException e) {
				SI.setResponse(ServletItem.Code.EMailNotSended, "Error when sending email. Check if the email is correct, or reload the page and try again.");
			}
		SI.sendResponse();
	}
}