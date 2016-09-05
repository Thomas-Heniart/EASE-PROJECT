package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class AskForNewApp
 */

public class AskForNewApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AskForNewApp() {
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
		
		String ask = request.getParameter("ask");
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		User user = null;
		user = (User)(request.getSession().getAttribute("User"));
		if (user == null) {
			Stats.saveAction(session.getServletContext(), user, Stats.Action.AskForNewApp, "");
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
			return ;
		} else if (db.connect() != 0){
			retMsg = "error: Impossible to connect data base.";
		} else if (Regex.isUrl(ask) == false){
			retMsg = "error: bad ask.";
		}
		else {
			db.set("INSERT INTO askForSite VALUES ('" + user.getEmail() + "', '" + ask + "');");
			retMsg = "success";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.AskForNewApp, retMsg);
		response.getWriter().write(retMsg);
	}
}
