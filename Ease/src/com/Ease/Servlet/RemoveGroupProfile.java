package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Context.Group.GroupProfile;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class RemoveGroupProfile
 */
@WebServlet("/RemoveGroupProfile")
public class RemoveGroupProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveGroupProfile() {
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
			sm.needToBeConnected();
			String single_id = sm.getServletParam("single_id", true);
			if (single_id == null || single_id.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "Client error.");
			@SuppressWarnings("unchecked")
			Map<String, GroupProfile> groupProfiles = (Map<String, GroupProfile>) sm.getContextAttr("groupProfiles");
			try {
				GroupProfile groupProfile = groupProfiles.get(Integer.parseInt(single_id));
				if (groupProfile == null)
					throw new GeneralException(ServletManager.Code.ClientError, "This group profile does not exist");
				groupProfile.removeFromDb(sm);
			} catch (NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
			
			sm.setResponse(ServletManager.Code.Success, "Group profile removed");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
