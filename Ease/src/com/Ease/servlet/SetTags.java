package com.Ease.servlet;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
import com.Ease.context.Tag;
import com.Ease.session.Account;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/setTags")
public class SetTags extends HttpServlet {

       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public SetTags() {
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
		
		String			websiteId = request.getParameter("websiteId");
		String			tagsId = request.getParameter("tagsId");
		
		String			retMsg;

		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		User user = (User)session.getAttribute("User");

		if(!user.isAdmin(session.getServletContext())){
			response.getWriter().print("error: You aint admin bro");
			return;
		}
		
		tagsId = tagsId.replaceAll("\"", "");
		tagsId = tagsId.substring(1, tagsId.length()-1);
		String[] tags = tagsId.split(",");
		int tagId;
		String dbRequest;
		
		db.set("START TRANSACTION;");
		
		dbRequest = "DELETE FROM TagAndSiteMap WHERE website_id="+ Integer.parseInt(websiteId) + ";";
		if(db.set(dbRequest)!=0){
			retMsg = "error: fail to connect to db";
			db.set("ROLLBACK;");
			response.getWriter().print(retMsg);
			return;
		}
		
		for(String tag : tags){
			tagId = Integer.parseInt(tag);
			dbRequest = "INSERT INTO TagAndSiteMap VALUES (" + tagId + "," + Integer.parseInt(websiteId) + ");";
			if(db.set(dbRequest)!=0){
				retMsg = "error: fail to connect to db";
				db.set("ROLLBACK;");
				response.getWriter().print(retMsg);
				return;
			}
		}
		
		db.set("COMMIT;");
	
		
		SiteManager siteManager = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
		
		try {
			siteManager.setTagsForSites(session.getServletContext());
		} catch (SQLException e) {
			System.out.println("Fail to load tags");
			return;
		}
		
		retMsg = "success";
		
		response.getWriter().print(retMsg);
	}
}
