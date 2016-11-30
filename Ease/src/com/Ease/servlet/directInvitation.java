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

import com.Ease.dashboard.Invitation;
import com.Ease.data.Regex;
import com.Ease.utils.DataBaseConnection;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

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

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ServletManager sm = null;
		try {
			sm = new ServletManager(this.getClass().getName(), request, response, true, false);

		} catch (Exception e1) {
			response.getWriter().println(e1.getMessage());
			return;
		}
		try {
			DataBaseConnection db = sm.getDB();
			String email = sm.getServletParam("email", true);
			if (email != null) {
				email.replaceAll(" ", "");
				email.replaceAll("\r", "");
				email.replaceAll("\n", "");
				email.replaceAll("\t", "");
			}
			ResultSet rs;
			if (email == null || Regex.isEmail(email) == false) {
				sm.setResponse(ServletManager.Code.UserMiss, "This is not an email.");
			} else {
				try {
					rs = db.get("select * from users where email='" + email + "';");
					if (rs.next())
						sm.setResponse(ServletManager.Code.UserMiss, "You already have an account.");
					else {
						Invitation.createInvitation(email, null, sm);
					}
				} catch (GeneralException e) {
					throw e;
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