package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EditGroup
 */
@WebServlet("/EditGroup")
public class EditGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditGroup() {
        super();
        // TODO Auto-generated constructor stub
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();

		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			}
			String groupId = sm.getServletParam("groupId", true);
			String groupName = sm.getServletParam("groupName", true);
			if (groupName == null || groupName.length() > 25) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong group name");
			}
			Group group;
			if (groupId == null || (group = GroupManager.getGroupManager(sm).getGroupFromSingleID(Integer.parseInt(groupId))) == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong group id");
			}
			group.setName(groupName, sm);
			sm.setResponse(ServletManager.Code.Success, "Group edited.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
