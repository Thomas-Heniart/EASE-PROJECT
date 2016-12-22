package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Dashboard.User.UserEmail;

/**
 * Servlet implementation class ConnectionServlet
 */
@WebServlet("/GetUserEmails")
public class GetUserEmails extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetUserEmails() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		Map<String, UserEmail> emails = user.getEmails();
		String emailString = "";
		String verified = "0";
		for(UserEmail email : emails.values()){
			if(email.equals(user.getEmail())){
				verified = "2";
			} else if(email.isVerified()){
				verified = "1";
			} else {
				verified = "0";
			}
			emailString += email.getEmail()+","+verified+";";
		}
		if(emailString.length()>0)
			emailString = emailString.substring(0, emailString.length()-1);
		response.getWriter().print(emailString);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		//
	}
	
}