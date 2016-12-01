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

import com.Ease.dashboard.Invitation;
import com.Ease.data.Regex;
import com.Ease.session.User;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

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
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();

		String email = sm.getServletParam("email", true);

		try {
			if (user != null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You are logged on Ease.");
			} else if (email == null || !Regex.isEmail(email))
				throw new GeneralException(ServletManager.Code.ClientWarning, "This is not an email");
			else {
				ResultSet rs = db.get("SELECT group_id FROM invitationsAndGroupsMap JOIN invitations ON invitationsAndGroupsMap.invitation_id = invitations.id WHERE email='" + email + "';");
				try {
					if (rs.next()) {
						Invitation.sendInvitation(email, rs.getString(1), sm);
						String retMsg = "1 You receveid an email";
						sm.setResponse(ServletManager.Code.Success, retMsg);
					} else {
						sm.setResponse(ServletManager.Code.Success, "2 Go to registration");
					}
				} catch (SQLException e) {
					throw new GeneralException(ServletManager.Code.InternError, e);
				}
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}