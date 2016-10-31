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
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addApp")
public class AddApp extends HttpServlet {

       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AddApp() {
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
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AddApp, request, response, user);
		
		// Get Parameters
		String profileIdParam = SI.getServletParam("profileId");
		String siteId = SI.getServletParam("siteId");
		String login = SI.getServletParam("login");
		String password = request.getParameter("password");
		String name = SI.getServletParam("name");
		// --
		
		Site site = null;
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
						
			int profileId = Integer.parseInt(profileIdParam);
			
			Profile profile = null;
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (login == null || login.equals("")) {
				SI.setResponse(ServletItem.Code.BadParameters, "Login can't be empty.");
			} else if (name.length() > 14) {
				SI.setResponse(ServletItem.Code.BadParameters, "Incorrect app name.");
			} else if (password == null || password.equals("")) {
				SI.setResponse(ServletItem.Code.BadParameters, "Password can't be empty.");
			} else if ((profile = user.getProfile(profileId)) == null){
				SI.setResponse(ServletItem.Code.BadParameters, "No profileId.");
			} else {
				if ((site = ((SiteManager)session.getServletContext().getAttribute("siteManager")).get(siteId)) == null) {
					SI.setResponse(ServletItem.Code.BadParameters, "This website doesn't exist in the database.");
				} else {
					if (profile.havePerm(Profile.ProfilePerm.ADDAPP, session.getServletContext()) == true){
						transaction = db.start();
						App app = new App(name, login, password, site, profile, user, session.getServletContext());
						profile.addApp(app);
						user.getApps().add(app);
						if (user.getTuto().equals("0")) {
							user.tutoComplete();
							user.updateInDB(session.getServletContext());
						}
						if (Regex.isEmail(login)) {
							db.set("CALL addEmail(" + user.getId() + ", '" + login + "');");
							user.addEmailIfNotPresent(login);
						}
						SI.setResponse(200, Integer.toString(app.getAppId()));
						db.commit(transaction);
					} else {
						SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
					}
				}
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (IndexOutOfBoundsException e){
			db.cancel(transaction);
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Numbers exception.");
		}
		SI.sendResponse();
	}
}