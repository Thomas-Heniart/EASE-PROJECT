package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/register")
public class Registration extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Registration() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		// Get Parameters
		String fname = sm.getServletParam("fname", true);
		String email = sm.getServletParam("email", true);
		String password = sm.getServletParam("password", false);
		String confirmPassword = sm.getServletParam("confirmPassword", false);

		try {
			if (user != null) {
				throw new GeneralException(ServletManager.Code.UserMiss, "You are logged on Ease.");
			} else if (fname == null || fname.length() < 2) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Your name is too short.");
			} else if (email == null || Regex.isEmail(email) == false) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Incorrect email.");
			} else if (password == null || Regex.isPassword(password) == false) {
				throw new GeneralException(ServletManager.Code.UserMiss,
						"Password is too short (at least 8 characters).");
			} else if (confirmPassword == null || password.equals(confirmPassword) == false) {
				throw new GeneralException(ServletManager.Code.UserMiss, "Passwords are not the same.");
			} else {
				// try {
				/*
				 * String group; rs = db.get(
				 * "select * from invitations where email ='" + email +
				 * "' and linkCode = '" + invitationCode + "';"); if (rs.next())
				 * { group = rs.getString(3); rs = db.get(
				 * "select * from users where email = '" + email +
				 * "' limit 0, 1;"); if (rs.next()) {
				 * SI.setResponse(ServletItem.Code.BadParameters,
				 * "You already have an account."); } else { lname = ""; user =
				 * new User(fname, lname, email, "0606060606", password,
				 * session.getServletContext()); if (group != null &&
				 * group.equals("null") == false) db.set(
				 * "insert into GroupAndUserMap values (NULL, " + group + ", " +
				 * user.getId() + ");");
				 * user.checkForGroup(session.getServletContext()); db.set(
				 * "delete from invitations where email = '" + email +
				 * "' and linkCode = '" + invitationCode + "';");
				 * session.setAttribute("User", user); db.set("CALL addEmail(" +
				 * user.getId() + ", '" + user.getEmail() + "');"); db.set(
				 * "UPDATE usersEmails SET verified = 1 WHERE user_id = " +
				 * user.getId() + " AND email = '" + user.getEmail() + "';");
				 * SessionSave sessionSave = new SessionSave(user,
				 * session.getServletContext()); session.setAttribute("User",
				 * user); session.setAttribute("SessionSave", sessionSave);
				 * SI.setResponse(200,
				 * "Successfull registration! You will be redirect in few seconds :)"
				 * ); } } else { SI.setResponse(ServletItem.Code.BadParameters,
				 * "You have no invitation or you already have an account."); }
				 * } catch (SessionException e) { e.printStackTrace();
				 * SI.setResponse(ServletItem.Code.LogicError,
				 * ServletItem.getExceptionTrace(e)); } catch (SQLException e) {
				 * e.printStackTrace();
				 * SI.setResponse(ServletItem.Code.LogicError,
				 * ServletItem.getExceptionTrace(e)); } }
				 */
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}