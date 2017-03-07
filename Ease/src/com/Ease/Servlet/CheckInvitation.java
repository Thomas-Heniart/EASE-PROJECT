package com.Ease.Servlet;

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

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Invitation;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class NewUser
 */
@WebServlet("/checkInvitation")
public class CheckInvitation extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckInvitation() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();

		String email = sm.getServletParam("email", true);
		String name = sm.getServletParam("name", true);

		try {
			if (user != null) {
				Logout.logoutUser(user, sm); //throw new GeneralException(ServletManager.Code.ClientWarning, "You are logged on Ease.");
			}
			if (email == null || !Regex.isEmail(email)) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "This is not an email.");
			} else if (name == null || name.length() < 2) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Name too short.");
			} else {
				try {
					ResultSet rs = db.get("SELECT id FROM users WHERE email = '" + email + "';");
					if (rs.next())
						throw new GeneralException(ServletManager.Code.ClientWarning, "You already have an account");
					rs = db.get("SELECT group_id FROM invitationsAndGroupsMap JOIN invitations ON invitationsAndGroupsMap.invitation_id = invitations.id WHERE email='" + email + "';");
					if (rs.next()) {
						Invitation.sendInvitation(email, name, null, sm);
						sm.setResponse(ServletManager.Code.Success, "1 You received an email");
					} else {
						if (Invitation.checkEmail(email, name, sm))
							sm.setResponse(ServletManager.Code.Success, "1 You received an email");
						else
							sm.setResponse(ServletManager.Code.Success, "2 Go to registration");
					}
				} catch (SQLException e) {
					throw new GeneralException(ServletManager.Code.InternError, e);
				}
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}