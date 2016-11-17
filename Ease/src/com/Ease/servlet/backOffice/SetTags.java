package com.Ease.servlet.backOffice;


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
import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

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
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.SetTags, request, response, user);
		
		// Get Parameters
		String websiteIdParam = SI.getServletParam("websiteId");
		String tagsId = SI.getServletParam("tagsId");
		// --
			
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if(!user.isAdmin(session.getServletContext())){
				SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission");
			} else {
				tagsId = tagsId.replaceAll("\"", "");
				tagsId = tagsId.substring(1, tagsId.length()-1);
				String[] tags = tagsId.split(",");
				
				int websiteId = Integer.parseInt(websiteIdParam);
				
				db.set("START TRANSACTION;");
				db.set("DELETE FROM TagAndSiteMap WHERE website_id="+ websiteId + ";");
				int tagId;
				for(String tag : tags){
					tagId = Integer.parseInt(tag);
					db.set("INSERT INTO TagAndSiteMap VALUES (" + tagId + "," + websiteId + ");");
				}
				db.set("COMMIT;");
				SiteManager siteManager = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
				siteManager.setTagsForSites(session.getServletContext());
				SI.setResponse(200, "Tag set");
			}
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad numbers.");
		}
		SI.sendResponse();
	}
}
