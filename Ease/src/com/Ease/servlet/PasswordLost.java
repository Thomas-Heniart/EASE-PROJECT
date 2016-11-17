package com.Ease.servlet;

import java.io.IOException;
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
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class PasswordLost
 */
@WebServlet("/PasswordLost")
public class PasswordLost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordLost() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = null;
		user = (User)(session.getAttribute("User"));
		if (user != null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		}
		else {
			RequestDispatcher rd = request.getRequestDispatcher("lostPassword.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.PasswordLost, request, response, user);
		
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
		
		try {
			if (user != null) {
				SI.setResponse(ServletItem.Code.AlreadyConnected, "You are logged on Ease.");
			} else if (email == null || Regex.isEmail(email) == false) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad email.");
			} else {
				ResultSet rs = db.get("select * from users where email='" + email + "';");
				if (rs.next()) {
					String userName = rs.getString(2);
					String linkCode = "";
					Random r = new Random();
					String			alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
					for (int i = 0;i < 126 ; ++i) {
						linkCode += alphabet.charAt(r.nextInt(alphabet.length()));			
					}
					rs = db.get("select * from PasswordLost where email='" + email + "';");
					if (rs.next()) {
						db.set("delete from PasswordLost where email='" + email + "';");
					}
					db.set("insert into PasswordLost values ('" + email + "', '" + linkCode + "');");
					Mail mail = new Mail();
					mail.sendPasswordLostMail(email, linkCode, userName);
					SI.setResponse(200, "Please, go check your email.");
				} else {
					SI.setResponse(199, "This email is not associate with an account.");
				}
			}
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (MessagingException e) {
			SI.setResponse(ServletItem.Code.EMailNotSended, "Impossible to send an Email yet, retry in few minutes. Sorry.");
		}
		SI.sendResponse();
	}
}
