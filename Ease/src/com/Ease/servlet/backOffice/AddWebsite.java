package com.Ease.servlet.backOffice;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addWebsite")
public class AddWebsite extends HttpServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String UPLOAD_DIRECTORY = "resources/websites/";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddWebsite() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		SiteManager siteManager = (SiteManager)session.getAttribute("siteManager");
		ServletItem SI = new ServletItem(ServletItem.Type.AddWebsite, request, response, user);
		
		// Get Parameters
		String url = SI.getServletParam("siteUrl");
		String name = SI.getServletParam("siteName");
		String homePage = SI.getServletParam("homePage");
		String folder = SI.getServletParam("siteFolder");
		String haveLoginButton = SI.getServletParam("haveLoginButton");
		String[] haveLoginWith = SI.getServletParamValues("haveLoginWith");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (!user.isAdmin(session.getServletContext())) {
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else {
			String dbRequest = "INSERT INTO websites VALUES (NULL, '" + url + "', '" + name + "', '" + UPLOAD_DIRECTORY + folder + "/', ";
			if(haveLoginButton != null){
				dbRequest += "1, ";
			} else {
				dbRequest += "0, ";
			}
			String haveLogWith = "";
			if(haveLoginWith == null || haveLoginWith.length == 0){
				haveLogWith = "NULL";
			} else {
				haveLogWith = "'";
				for(String i : haveLoginWith){
					haveLogWith += i + ","; 
				}
				haveLogWith = haveLogWith.substring(0, haveLogWith.length()-1);
				haveLogWith += "'";
			}
			dbRequest = dbRequest + haveLogWith + ", null, 0, '" + homePage + "', 0, default, default, default, default, default);";
			db.set(dbRequest);
			SiteManager sites = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
			try {
			    ResultSet rs = db.get("SELECT * FROM websites ORDER BY website_name;");
			    sites.clearSites();
			    while (rs.next()) {
			    	sites.add(new Site(rs, db, true));
			    }
			    siteManager.refresh(db);
			    SI.setResponse(200, "Site added.");
			} catch (SQLException e) {
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			}
		}
		SI.sendResponse();
	}
}
