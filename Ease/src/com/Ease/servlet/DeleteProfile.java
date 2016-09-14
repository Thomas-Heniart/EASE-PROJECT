package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class DeleteProfile
 */

public class DeleteProfile extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteProfile() {
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
		Profile profile = null;
		
		try {
			int index = Integer.parseInt(request.getParameter("index"));
			String mdp = request.getParameter("password");
			
			
			DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
			
			
			user = (User)(session.getAttribute("User"));
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.DeleteProfile, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if ((profile = user.getProfile(index)) == null){
				retMsg = "error: Bad id.";
			} else if (mdp == null || !Hashing.SHA(mdp, user.getSaltEase()).equals(user.getPassword())) {
				retMsg = "error: Bad password";
			} else {
				profile.deleteFromDB(session.getServletContext());
				user.getProfiles().remove(profile.getIndex());
				user.updateIndex(session.getServletContext());
				retMsg = "success";
			}
		} catch (SessionException e) {
			retMsg = "error :" + e.getMsg();
		} catch (NumberFormatException e) {
			retMsg = "error: Bad index";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.DeleteProfile, retMsg);
		response.getWriter().print(retMsg);
	}
}
