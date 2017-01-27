package com.Ease.Servlet.BackOffice;

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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetGroupUsers
 */
@WebServlet("/GetGroupUsers")
public class GetGroupUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGroupUsers() {
        super();
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
	@SuppressWarnings("unchecked")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();

		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			}
			String paramGroupId = sm.getServletParam("groupId", true);
			if (paramGroupId == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "groupId is null");
			int groupSingleId = Integer.parseInt(paramGroupId);
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			String group_id = groupManager.getGroupFromSingleID(groupSingleId).getDBid();
			JSONArray res = new JSONArray();
			ResultSet rs = db.get("SELECT firstName, email FROM users WHERE id IN (SELECT user_id from groupsAndUsersMap WHERE group_id = " + group_id + ")");
			try {
				while (rs.next()) {
					JSONObject tmp = new JSONObject();
					tmp.put("name", rs.getString(1));
					tmp.put("email", rs.getString(2));
					res.add(tmp);
				}
				sm.setResponse(ServletManager.Code.Success, res.toString());
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
