package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupProfile;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class AddGroup
 */
@WebServlet("/AddGroup")
public class AddGroup extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGroup() {
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		
		try {
			String name = sm.getServletParam("name", true);
			String parent_id = sm.getServletParam("parent_id", true);
			Group parent = null;
			Infrastructure infra = null;
			Map<Integer, Group> groups = (Map<Integer, Group>) sm.getContextAttr("groups");
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Name is empty.");
			sm.needToBeConnected();
			if (parent_id != null) {
				parent = groups.get(Integer.parseInt(parent_id));
				infra = parent.getInfra();
			}
			Group newGroup = Group.createGroup(name, parent, infra, sm);
			groups.put(newGroup.getSingleId(), newGroup);
			GroupProfile groupProfile = groupProfiles.get(Integer.parseInt(single_id));
			sm.setResponse(ServletManager.Code.Success, "Group profile removed");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
