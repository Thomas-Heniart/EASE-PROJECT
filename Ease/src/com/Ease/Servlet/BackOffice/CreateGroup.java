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
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class CreateGroup
 */
@WebServlet("/CreateGroup")
public class CreateGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGroup() {
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
		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			}
			String infraId = sm.getServletParam("infraId", true);
			String parentId = sm.getServletParam("parentId", true);
			String groupName = sm.getServletParam("groupName", true);
			Infrastructure infra;
			if (groupName == null || groupName.length() > 25) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong infrastructure name.");
			} else if (infraId == null || (infra = GroupManager.getGroupManager(sm).getInfraFromSingleID(Integer.parseInt(infraId))) == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong infrastructure id.");
			}
			Group parent = null;
			if (parentId != null && (parent = GroupManager.getGroupManager(sm).getGroupFromSingleID(Integer.parseInt(parentId))) == null) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong parent group id.");
			}
			Group group = Group.createGroup(groupName, parent, infra, sm);
			
			sm.setResponse(ServletManager.Code.Success, Integer.toString(group.getSingleId()));
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
