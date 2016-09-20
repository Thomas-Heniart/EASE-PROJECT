package com.Ease.servlet;
import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.session.User;
import com.Ease.stats.Stats;
import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;

//import com.mysql.fabric.xmlrpc.base.Member;


/**
 * Servlet implementation class AddProfile
 */
public class AddProfile extends HttpServlet {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AddProfile() {
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
		
		String name = request.getParameter("name");
		String color = request.getParameter("color");
		
		HttpSession session = request.getSession();
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		String retMsg;
		
		User user = (User)(session.getAttribute("User"));
		if (user == null){
			Stats.saveAction(session.getServletContext(), user, Stats.Action.AddProfile, "");
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
			return ;
		} else if (db.connect() != 0){
			retMsg = "error: Impossible to connect data base.";
		} else if (name == null || name == ""){
			retMsg = "error: Bad profile's name.";
		} else if (color == null || Regex.isColor(color) == false){
			retMsg = "error: Bad profile's color.";
		} else {
			try {
				transaction = db.start();
				Profile profile = new Profile(name, color, "", user, session.getServletContext());
				user.addProfile(profile);
				retMsg = "success: " + profile.getProfileId();
				db.commit(transaction);
			} catch (SessionException e) {
				db.cancel(transaction);
				retMsg = "error: " + e.getMsg(); ;
			}
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.AddProfile, retMsg);
		response.getWriter().print(retMsg);
	}
}