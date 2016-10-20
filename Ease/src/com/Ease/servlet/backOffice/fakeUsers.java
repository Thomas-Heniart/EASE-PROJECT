package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class fakeUsers
 */
@WebServlet("/fakeUsers")
public class fakeUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public fakeUsers() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		User user = (User) session.getAttribute("user");
		ServletItem SI = new ServletItem(ServletItem.Type.ResetUser, request, response, user);
		if (user == null || !user.isAdmin(getServletContext())) {
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
		} else {
			if (db.connect() != 0) {
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "Connection failed");
				SI.sendResponse();
			}
			Set<Integer> idsToRemove = new HashSet<Integer>();
			ResultSet profilesWithoutApps = db.get(
					"SELECT DISTINCT user_id FROM profiles left join apps on apps.profile_id = profiles.profile_id WHERE apps.profile_id IS NULL;");
			ResultSet noProfiles = db.get(
					"SELECT DISTINCT users.user_id FROM users LEFT JOIN profiles ON users.user_id = profiles.user_id WHERE profiles.user_id IS NULL AND users.user_id < 288");
			try {
				String user_id = null;
				while (profilesWithoutApps.next()) {
					user_id = profilesWithoutApps.getString(1);
					if (Integer.parseInt(user_id) < 288) {
						Boolean hasApps = false;
						ResultSet userProfiles = db
								.get("SELECT profile_id FROM profiles WHERE user_id=" + user_id + ";");
						while (userProfiles.next()) {
							String profile_id = userProfiles.getString(1);
							if (db.get("SELECT * FROM apps WHERE profile_id = " + profile_id + ";").next()) {
								hasApps = true;
								break;
							}
						}
						if (!hasApps)
							idsToRemove.add(Integer.parseInt(user_id));
					}
				}
				while (noProfiles.next()) {
					user_id = noProfiles.getString(1);
					idsToRemove.add(Integer.parseInt(user_id));
				}
				Iterator<Integer> it = idsToRemove.iterator();
				while (it.hasNext()) {
					int uId = it.next();
					ResultSet email = db.get("SELECT email FROM users WHERE user_id = "+ uId + ";");
					if (email.next())
						if (email.getString(1).contains("@ieseg.fr")) {
							db.set("DELETE FROM profiles where user_id = " + uId + ";");
							db.set("DELETE FROM GroupAndUserMap where user_id = " + uId + ";");
							db.set("DELETE FROM savedSessions where user_id = " + uId + ";");
							db.set("DELETE FROM usersEmails where user_id = " + uId + ";");
							db.set("DELETE FROM users where user_id = " + uId + ";");
						}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			db.close();
			response.getWriter().append("Removed all useless users");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
