package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class MoveProfile
 */
@WebServlet("/moveProfile")
public class MoveProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoveProfile() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.MoveProfile, request, response, user);
		
		// Get Parameters
		String profileIdParam = SI.getServletParam("profileId");
		String indexParam = SI.getServletParam("index");
		String columnIdxParam = SI.getServletParam("columnIdx");
		String profileIdxParam = SI.getServletParam("profileIdx");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		boolean transaction = false;
		Profile profile = null;
		
		try {
			int profileId = Integer.parseInt(profileIdParam);
			int index = Integer.parseInt(indexParam);
			int profileIdx = Integer.parseInt(profileIdxParam);
			int columnIdx = Integer.parseInt(columnIdxParam);
				
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (columnIdx < 1 || columnIdx > 4){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad columnIdx.");
			} else if (profileIdx < 1 || profileIdx >= user.getProfilesDashboard().get(columnIdx).size()){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileIdx.");
			} else if ((profile = user.getProfile(profileId)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileId.");
			} else if (profile.getId() == user.getProfilesDashboard().get(0).get(0).getId()) {
				SI.setResponse(ServletItem.Code.LogicError, "Trying to move side profile.");	
			} else {
				if (profile.havePerm(Profile.ProfilePerm.MOVE, session.getServletContext())){
					transaction = db.start();
					user.moveProfileAt(session.getServletContext(), profile.getIndex(), index);
					user.moveProfileAt(session.getServletContext(), profile, profileIdx, columnIdx);
					db.commit(transaction);
					SI.setResponse(200, "Profile moved.");
				} else {
					SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission");
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
