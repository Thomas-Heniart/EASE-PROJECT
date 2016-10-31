package com.Ease.servlet;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;


/**
 * Servlet implementation class NewUser
 */
@WebServlet("/registerInv")
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
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("User");
		if (user != null) {
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("registrationByInvitation.jsp");
			rd.forward(request, response);
		}
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
			SI.setResponse(ServletItem.Code.BadParameters, "Bad fist name.");	
		} else if (lname == null || lname.length() < 2){
			SI.setResponse(ServletItem.Code.BadParameters, "Bad last name.");
		} else if (email == null || Regex.isEmail(email) == false){
			SI.setResponse(ServletItem.Code.BadParameters, "Bad email.");
		} else if (password == null || Regex.isPassword(password) == false) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad password.");
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
						SI.setResponse(ServletItem.Code.BadParameters, "Email already used.");
					} else {
						user = new User(fname, lname, email, "0606060606", password, session.getServletContext());
						if (group != null && group.equals("null") == false)
							db.set("insert into GroupAndUserMap values (NULL, " + group + ", " + user.getId() + ");");
						db.set("delete from invitations where email = '" + email + "' and linkCode = '" + invitationCode + "';");
						session.setAttribute("User", user);
						db.set("CALL addEmail(" + user.getId() + ", '" + user.getEmail() + "');");
						db.set("UPDATE usersEmais SET verified = 1 WHERE user_id = " + user.getId() + " AND email = '" + user.getEmail() + "';");
						SI.setResponse(200, "User registered.");
					}
				} else {
					SI.setResponse(ServletItem.Code.BadParameters, "You have no invitation or you have already an account.");
				}
			} catch (SessionException e) {
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			} catch (SQLException e) {
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			}
		}
		SI.sendResponse();
	}
}