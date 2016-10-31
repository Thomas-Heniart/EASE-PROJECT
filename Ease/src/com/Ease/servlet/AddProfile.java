package com.Ease.servlet;
import java.io.IOException;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.session.User;
import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;

//import com.mysql.fabric.xmlrpc.base.Member;


/**
 * Servlet implementation class AddProfile
 */
@WebServlet("/addProfile")
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
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AddProfile, request, response, user);
		
		// Get Parameters
		String name = SI.getServletParam("name");
		String color = SI.getServletParam("color");
		// --
		
		boolean transaction = false;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		if (user == null){
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
		} else if (db.connect() != 0){
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
		} else if (user.getProfiles().size() > 16){
			SI.setResponse(ServletItem.Code.LogicError, "Too many profiles.");
		} else if (name == null || name == ""){
			SI.setResponse(ServletItem.Code.BadParameters, "Empty profile name.");
		} else if (color == null || Regex.isColor(color) == false){
			SI.setResponse(ServletItem.Code.BadParameters, "Incorrect profile color.");
		} else {
			try {
				transaction = db.start();
				Profile profile = new Profile(name, color, "", user, null, session.getServletContext(), false);
				user.addProfile(profile);
				SI.setResponse(200, Integer.toString(profile.getProfileId()));
				db.commit(transaction);
			} catch (SessionException e) {
				db.cancel(transaction);
				SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			}
		}
		SI.sendResponse();
	}
}