package com.Ease.servlet;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */

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
		// TODO Auto-generated constructor stub
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
		String			url = request.getParameter("siteUrl");
		String			name = request.getParameter("siteName");
		String			folder = request.getParameter("siteFolder");
		String 			haveLoginButton = request.getParameter("haveLoginButton");
		String[]		haveLoginWith = request.getParameterValues("haveLoginWith");
		String			retMsg;

		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		User user = (User)session.getAttribute("User");

		if(!user.isAdmin(session.getServletContext())){
			response.getWriter().print("error: You aint admin bro");
			return;
		}

		String dbRequest = "INSERT INTO websites VALUES (NULL, '" + url + "', '" + name + "', '" + UPLOAD_DIRECTORY + folder + "/', ";
		if(haveLoginButton != null){
			dbRequest += "1, ";
		} else {
			dbRequest += "0, ";
		}

		String haveLogWith = "";

		if(haveLoginWith == null || haveLoginWith.length==0){
			haveLogWith = "NULL";
		}
		else {
			haveLogWith = "'";
			for(String i : haveLoginWith){
				haveLogWith += i + ","; 
			}
			haveLogWith = haveLogWith.substring(0, haveLogWith.length()-1);
			haveLogWith += "'";
		}

		dbRequest = dbRequest + haveLogWith + ");";

		if(db.set(dbRequest)!=0){
			retMsg = "error: fail to connect to db";
			response.getWriter().print(retMsg);
			return;
		}
		
		SiteManager sites = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
		
		try {
		    	ResultSet rs = db.get("SELECT * FROM websites ORDER BY website_name;");
		    	sites.clearSites();
		    	while (rs.next()) {
		    		sites.add(new Site(rs));
		    	}
		} catch (SQLException e) {
				retMsg = "error: fail to load websites";
				response.getWriter().print(retMsg);
		    	return ;
		}
		
		retMsg = "success";
		
		response.getWriter().print(retMsg);
		
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}
}
