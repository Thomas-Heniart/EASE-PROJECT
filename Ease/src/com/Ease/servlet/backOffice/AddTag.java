package com.Ease.servlet.backOffice;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.Ease.context.DataBase;
import com.Ease.context.SiteManager;
import com.Ease.context.Tag;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AddApp
 */
@WebServlet("/addTag")
public class AddTag extends HttpServlet {

       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public AddTag() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.AddTag, request, response, user);
		
		// Get Parameters
		String name = SI.getServletParam("tagName");
		String color = SI.getServletParam("tagColor");
		// --
		
		int nbOfColors = 8;		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (!user.isAdmin(session.getServletContext())) {
			SI.setResponse(ServletItem.Code.NoPermission, "You have not the permission.");
		} else if (name == null || name.equals("")) {
			SI.setResponse(ServletItem.Code.BadParameters, "Bad tag name.");
		} else {
			if (color == null || color.equals("0")) {
				Random r = new Random();
				color = Integer.toString(1 + r.nextInt(nbOfColors));
			}
			if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else {
				db.set("INSERT INTO tags VALUES (NULL, '" + name + "'," + color + ");");
				SiteManager sites = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
				try {
					ResultSet rs = db.get("SELECT * FROM tags;");
					sites.clearTags();
					while (rs.next()) {
						sites.addNewTag(new Tag(rs, session.getServletContext()));
					}
					SI.setResponse(200, "Tag added.");
				} catch (SQLException e) {
					SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
				}
			}
		}
		SI.sendResponse();
	}
}
