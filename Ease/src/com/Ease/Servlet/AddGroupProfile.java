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
import com.Ease.Context.Group.GroupProfile;
import com.Ease.Context.Group.ProfilePermissions;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.Regex;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class AddGroupProfile
 */
@WebServlet("/AddGroupProfile")
public class AddGroupProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddGroupProfile() {
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
		
		try {
			String name = sm.getServletParam("name", true);
			String color = sm.getServletParam("color", true);
			boolean common = Boolean.parseBoolean(sm.getServletParam("common", true));
			int perms = Integer.parseInt(sm.getServletParam("perms", true));
			String group_id = sm.getServletParam("group_id", true);
			if (name == null || name.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Name empty.");
			else if (color == null || Regex.isColor(color) == false)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong color.");
			else if (group_id == null || group_id.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Group error.");
			sm.needToBeConnected();
			Map<String, Group> groups = (Map<String, Group>) sm.getContextAttr("groups");
			Group group = groups.get(group_id);
			GroupProfile newGroupProfile = GroupProfile.createGroupProfile(group, perms, name, color, common, sm);
			sm.setResponse(ServletManager.Code.Success, newGroupProfile.getJSONString());
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
