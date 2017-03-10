package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;

import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetGrouApps
 */
@WebServlet("/GetGrouApps")
public class GetGrouApps extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetGrouApps() {
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
			if (user.isAdmin() == false) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "You need to be admin to do that.");
			}
			String paramGroupId = sm.getServletParam("groupId", true);
			if (paramGroupId == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "groupId is null");
			int groupSingleId = Integer.parseInt(paramGroupId);
			GroupManager groupManager = (GroupManager) sm.getContextAttr("groupManager");
			Group group = groupManager.getGroupFromSingleID(groupSingleId);
			List<GroupApp> groupApps = group.getGroupApps();
			JSONArray res = new JSONArray();
			for(GroupApp groupApp : groupApps)
				res.add(groupApp.getJson());
			sm.setResponse(ServletManager.Code.Success, res.toString());
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
