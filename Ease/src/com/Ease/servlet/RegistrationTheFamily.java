package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.dashboard.User;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/thefamily")
public class RegistrationTheFamily extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationTheFamily() {
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
			dispatch = "TheFamilyInvitation.jsp";
		else if (user != null)
			dispatch = "index.jsp";
		else
			dispatch = "TheFamilyRegistration.jsp";
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true, false);
		DataBaseConnection db = sm.getDB();
		try {
			String invitationCode = sm.getServletParam("invitationCode", false);
			String fname = sm.getServletParam("fname", true);
			String email = sm.getServletParam("email", true);
			String password = sm.getServletParam("password", false);
			String confirmPassword = sm.getServletParam("confirmPassword", false);

			if (user != null)
				throw new GeneralException(ServletManager.Code.ClientError, "You are logged on Ease.");
			else if (fname == null || fname.length() < 2)
				throw new GeneralException(ServletManager.Code.UserMiss, "Your name is too short.");
			else if (email == null || Regex.isEmail(email) == false)
				throw new GeneralException(ServletManager.Code.UserMiss, "Incorrect email.");
			else if (password == null || Regex.isPassword(password) == false)
				throw new GeneralException(ServletManager.Code.UserMiss,
						"Password is too short (at least 8 characters).");
			else if (confirmPassword == null || password.equals(confirmPassword) == false)
				throw new GeneralException(ServletManager.Code.UserMiss, "Passwords are not the same.");
			else if (invitationCode == null) {
				throw new GeneralException(ServletManager.Code.InternError, "No invitation code");
			} else {
				User newUser = User.createUser(email, fname, "", confirmPassword, invitationCode, sm);

			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}