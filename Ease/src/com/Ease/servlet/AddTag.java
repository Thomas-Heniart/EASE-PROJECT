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
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

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
		
		String			name = request.getParameter("tagName");
		String			color = request.getParameter("tagColor");
		int				nbOfColors = 8;
		
		String			retMsg;

		HttpSession session = request.getSession();
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		User user = (User)session.getAttribute("User");

		if(!user.isAdmin(session.getServletContext())){
			response.getWriter().print("error: You aint admin bro");
			return;
		}
		
		if(name.equals("")){
			response.getWriter().print("error: no tag name");
			return;
		}

		if (color.equals("0")){
			Random r = new Random();
			color = Integer.toString(1 + r.nextInt(nbOfColors));
		}
		

		String dbRequest = "INSERT INTO tags VALUES (NULL, '" + name + "'," + color + ");";

		if(db.set(dbRequest)!=0){
			retMsg = "error: fail to connect to db";
			response.getWriter().print(retMsg);
			return;
		}
		
		SiteManager sites = ((SiteManager)session.getServletContext().getAttribute("siteManager"));
		
		
		try {
			ResultSet rs = db.get("SELECT * FROM tags;");
			sites.clearTags();
			while (rs.next()) {
				sites.addNewTag(new Tag(rs, session.getServletContext()));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load tags");
			return;
		}
		
		retMsg = "success";
		
		response.getWriter().print(retMsg);
		
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}
}
