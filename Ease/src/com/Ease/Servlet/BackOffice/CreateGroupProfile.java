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
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.Profile.ProfilePermissions;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class CreateGroupProfile
 */
@WebServlet("/CreateGroupProfile")
public class CreateGroupProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateGroupProfile() {
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		try {
			sm.needToBeConnected();
			if (user.isAdmin() == false)
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			String paramGroupId = sm.getServletParam("groupId", true);
			String profileName = sm.getServletParam("profileName", true);
			if (paramGroupId == null || paramGroupId.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "groupId is null");
			if (profileName == null || profileName.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "profile name is null");
			
			int groupSingleId = Integer.parseInt(paramGroupId);
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			Group group = groupManager.getGroupFromSingleID(groupSingleId);
			GroupProfile groupProfile = GroupProfile.createGroupProfile(group, ProfilePermissions.Perm.ALL.getValue(), profileName, "1", false, sm);
			group.addGroupProfile(groupProfile);
			groupManager.add(groupProfile);
			sm.setResponse(ServletManager.Code.Success, groupProfile.getJson().toString());
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
