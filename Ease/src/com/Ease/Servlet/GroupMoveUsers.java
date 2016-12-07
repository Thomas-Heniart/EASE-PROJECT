package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.Group;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GroupMoveUsers
 */
@WebServlet("/GroupMoveUsers")
public class GroupMoveUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupMoveUsers() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			String s_id_src = sm.getServletParam("s_id_src", true);
			String s_id_dst = sm.getServletParam("s_id_dst", true);
			String[] emails = sm.getServletParamArray("emails", true);
			@SuppressWarnings("unchecked")
			Map<Integer, Group> groups = (Map<Integer, Group>)sm.getContextAttr("groups");
			try {
				Group groupSrc = groups.get(Integer.parseInt(s_id_src));
				Group groupDst = groups.get(Integer.parseInt(s_id_dst));
				if (groupSrc == null || groupDst == null)
					throw new GeneralException(ServletManager.Code.ClientError, "Group source or group destination is null");
				groupSrc.getInfra().isAdmin(user, sm);
				groupDst.getInfra().isAdmin(user, sm);
				int transaction = db.startTransaction();
				for (String email : emails) {
					if (! Regex.isEmail(email))
						throw new GeneralException(ServletManager.Code.ClientError, "Wrong user email.");
					groupDst.addUser(email, sm);
					groupSrc.removeUser(email, sm);
				}
				db.commitTransaction(transaction);
				sm.setResponse(ServletManager.Code.Success, "Users moved from group source to group destination.");
			} catch (NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}
