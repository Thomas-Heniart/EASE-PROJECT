package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.Group;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GroupAddUsers
 */
@WebServlet("/groupAddUsers")
public class GroupAddUsers extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GroupAddUsers() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		
		try {
			sm.needToBeConnected();
			String single_id = sm.getServletParam("single_id", true);
			String[] emails = sm.getServletParamArray("emails", true);
			@SuppressWarnings("unchecked")
			Map<Integer, Group> groups = (Map<Integer, Group>)sm.getContextAttr("groups");
			Group group = groups.get(Integer.parseInt(single_id));
			if (group == null) {
				sm.setResponse(ServletManager.Code.ClientError, "This group dosen't exist.");
			} else {
				group.getInfra().isAdmin(user, sm);
				DataBaseConnection db = sm.getDB();
				int transaction = db.startTransaction();
				for (String email : emails) {
					group.addUser(email, sm);
				}
				db.commitTransaction(transaction);
				sm.setResponse(ServletManager.Code.Success, "Users added.");
			}
			
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (NumberFormatException e) {
			sm.setResponse(ServletManager.Code.ClientError, "Wrong single_id.");
		}
		sm.sendResponse();
	}
}
