package com.Ease.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class MoveApp
 */
@WebServlet("/moveApp")
public class MoveApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoveApp() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.MoveApp, request, response, user);
		
		// Get Parameters
		String appIdParam = SI.getServletParam("appId");
		String profileIdParam = SI.getServletParam("profileId");
		String indexParam = SI.getServletParam("index");
		
		// --
		
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
			int appId = Integer.parseInt(appIdParam);
			int profileId = Integer.parseInt(profileIdParam);
			int index = Integer.parseInt(indexParam);
			App app = null;
			Profile profile = null;
				
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if ((app = user.getApp(appId)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad appId");
			} else if ((profile = user.getProfile(profileId)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad profileId");
			} else if (index < 0 || index > profile.getApps().size()){
				SI.setResponse(ServletItem.Code.BadParameters, "Bad index");
			} else {
				Profile profileBegin = user.getProfile(app.getProfileId());
				
				if (app.havePerm(App.AppPerm.MOVE, session.getServletContext()) && (profile.getProfileId() == profileBegin.getProfileId()) || app.havePerm(App.AppPerm.CHANGEPROFILE, session.getServletContext())){
					profileBegin.getApps().remove(app.getIndex());
					profile.getApps().add(index, app);
					transaction = db.start();
					profileBegin.updateIndex(session.getServletContext());
					profile.updateIndex(session.getServletContext());
					if (profile.getProfileId() != profileBegin.getProfileId())
						app.updateProfileInDB(session.getServletContext(), profile.getId(), profile.getProfileId());
					SI.setResponse(200, "App moved.");
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
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();	
	}
}
