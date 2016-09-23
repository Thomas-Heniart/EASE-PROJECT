package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.session.App;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class AddLogWith
 */
public class AddLogWith extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddLogWith() {
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
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		Site site = null;
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {			
			int profileId = Integer.parseInt(request.getParameter("profileId"));
			String siteId = request.getParameter("siteId");
			int appId = Integer.parseInt(request.getParameter("appId"));
			String	name	=	request.getParameter("name");
			
			user = (User)(session.getAttribute("User"));
			Profile profile = null;
			
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.AddApp, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if ((profile = user.getProfile(profileId)) == null){
				retMsg = "error: Bad profileId.";
			} else if (name == null || name.length() > 14) {
				retMsg = "error: Incorrect name";
			} else if (user.getApp(appId) == null) {
				retMsg = "error: Bad appId.";
			} else if (user.getApp(appId).getType().equals("ClassicAccount") == false){
				retMsg = "error: This account is not an account.";
			} else {
				
				if ((site = ((SiteManager)session.getServletContext().getAttribute("siteManager")).get(siteId)) == null) {
					retMsg = "error: This site dosen't exist.";
				} else {
					if (profile.havePerm(Profile.ProfilePerm.ADDAPP, session.getServletContext())){
						transaction = db.start();
						App logWith = new App(name, user.getApp(appId).getId(), site, profile, user, session.getServletContext());
						profile.addApp(logWith);
						user.getApps().add(logWith);
						if (user.getTuto().equals("0")) {
							user.tutoComplete();
							user.updateInDB(session.getServletContext());
						}
						retMsg = "success: " + logWith.getAppId();
						db.commit(transaction);
					} else {
						retMsg = "error: You have not the permission";
					}
				}
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			retMsg = "error: " + e.getMsg();
		} catch (IndexOutOfBoundsException e){
			db.cancel(transaction);
			retMsg = "error: Bad app index.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.AddApp, retMsg + " : " + ((site != null) ? site.getName() : ""));
		response.getWriter().print(retMsg);
	}

}
