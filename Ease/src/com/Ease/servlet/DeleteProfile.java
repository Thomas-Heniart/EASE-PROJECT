package com.Ease.servlet;


import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class DeleteProfile
 */
@WebServlet("/deleteProfile")
public class DeleteProfile extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteProfile() {
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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.DeleteProfile, request, response, user);
		
		// Get Parameters
		String mdp = request.getParameter("password");
		String indexParam = SI.getServletParam("index");
		// --
		
		Profile profile = null;
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		try {
			int index = Integer.parseInt(indexParam);

			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if ((profile = user.getProfile(index)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Incorrect profileId.");
			} else if (profile.getId() == user.getProfilesDashboard().get(0).get(0).getId()) {
				SI.setResponse(ServletItem.Code.LogicError, "Trying to remove side profile.");
			} else if ((mdp != null && mdp != "") && !Hashing.SHA(mdp, user.getSaltEase()).equals(user.getHashedPassword())) {
				SI.setResponse(ServletItem.Code.BadParameters, "Incorrect password.");
			} else if ((mdp == null || mdp == "") && profile.getApps().size() != 0){
				SI.setResponse(ServletItem.Code.BadParameters, "Empty password.");
			} else {
				if (profile.havePerm(Profile.ProfilePerm.DELETE, session.getServletContext())){
					transaction = db.start();
					profile.deleteFromDB(session.getServletContext());
					user.getProfiles().remove(profile.getIndex());
					for (int i = 0; i < 5; ++i) {
						user.getProfilesDashboard().get(i).remove(profile);
					}
					user.updateIndex(session.getServletContext());
					SI.setResponse(200, "Profile deleted.");
					db.commit(transaction);
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException | SQLException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			try {
				db.cancel(transaction);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			SI.setResponse(ServletItem.Code.BadParameters, "Numbers exception.");
		}
		SI.sendResponse();
	}
}
