package com.Ease.Servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class RemoveProfile
 */
@WebServlet("/RemoveProfile")
public class RemoveProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveProfile() {
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
			String single_id = sm.getServletParam("single_id", true);
			if (single_id == null)
				throw new GeneralException(ServletManager.Code.ClientError, "Single_id empty");
			sm.needToBeConnected();
			removeProfile(user.getProfilesColumn(), Integer.parseInt(single_id), sm);
			user.updateProfilesIndex(sm);
			sm.setResponse(ServletManager.Code.Success, "Profile removed");
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
	
	public void removeProfile(List<List<Profile>> profiles, int single_id, ServletManager sm) throws GeneralException {
		Iterator<List<Profile>> it = profiles.iterator();
		while (it.hasNext()) {
			List<Profile> rows = it.next();
			Iterator<Profile> it2 = rows.iterator();
			while (it2.hasNext()) {
				Profile tmpProfile = it2.next();
				if (tmpProfile.getSingleId() == single_id) {
					tmpProfile.removeFromDB(sm);
					rows.remove(tmpProfile);
					return;
				}
			}
		}
		throw new GeneralException(ServletManager.Code.InternError, "Cannot remove this profile");
	}

}
