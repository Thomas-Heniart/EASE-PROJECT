package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class DeleteProfile
 */

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
		String mdp = SI.getServletParam("password");
		String indexParam = SI.getServletParam("index");
		// --
		
		Profile profile = null;
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			int index = Integer.parseInt(indexParam);

			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if ((profile = user.getProfile(index)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileId.");
			} else if (mdp == null || !Hashing.SHA(mdp, user.getSaltEase()).equals(user.getPassword())) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad password.");
			} else {
				if (profile.havePerm(Profile.ProfilePerm.DELETE, session.getServletContext())){
					transaction = db.start();
					profile.deleteFromDB(session.getServletContext());
					user.getProfiles().remove(profile.getIndex());
					user.updateIndex(session.getServletContext());
					SI.setResponse(200, "Profile deleted.");
					db.commit(transaction);
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
				}
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}
}
