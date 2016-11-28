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
 * Servlet implementation class EditUserEmail
 */
@WebServlet("/checkVerifiedEmail")
public class checkVerifiedEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public checkVerifiedEmail() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.checkVerifiedEmail, request, response, user);
		
		// Get Parameters
		String email = SI.getServletParam("email");
		// --
			
		
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
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
			SI.setResponse(ServletItem.Code.BadParameters, "Bad Email");
		} else if (user.getEmails().containsKey(email) == false) {
			SI.setResponse(ServletItem.Code.LogicError, "This is not one of your Email");
		} else if (user.getEmails().get(email).booleanValue() == false) {
			try {
				sendEmail(email, db, user);
			} catch (SQLException | MessagingException e) {
				e.printStackTrace();
				SI.setResponse(ServletItem.Code.EMailNotSended, "Fail to send mail.");
			}
			SI.setResponse(200, "1 An email has been send.");
		} else {
			SI.setResponse(200, "2 Your email is already valided.");
		}
		SI.sendResponse();
	}
	
	public void sendEmail(String email, DataBase db, User user) throws SQLException, MessagingException, UnsupportedEncodingException {
		String verificationCode = null;
		ResultSet rs = db.get("SELECT verificationCode FROM usersEmails WHERE email = '" + email + "' AND user_id = " + user.getId() + ";");
			if (rs.next())
				verificationCode = rs.getString(1);
			else {
				String alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
				Random r = new Random();
				for (int i = 0; i < 126; ++i)
					verificationCode += alphabet.charAt(r.nextInt(alphabet.length()));
				db.set("UPDATE usersEmails SET verificationCode = '" + verificationCode + "' WHERE user_id = " + user.getId() + " AND email = '" + email + "';");
			}
			String link = "https://ease.space/AddEmail?email=" + email + "&code=" + verificationCode;
			Mail newMail = new Mail();
			newMail.sendVerificationEmail(email, link);
	}
}
