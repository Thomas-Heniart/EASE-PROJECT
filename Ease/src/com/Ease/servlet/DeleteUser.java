package com.Ease.servlet;


import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class DeleteUser
 */

public class DeleteUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteUser() {
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
		String password = request.getParameter("password");
		
		HttpSession session = request.getSession();
		ServletContext context = session.getServletContext();
		User user = (User)(session.getAttribute("User"));
		boolean transaction = false;
		DataBase db = (DataBase)context.getAttribute("DataBase");
		
		String retMsg;
		try {
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.DeleteUser, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if (password == null || !user.getPassword().equals(Hashing.SHA(password, user.getSaltEase()))){
				retMsg = "error: Bad password.";
			} else {
				transaction = db.start();
				user.deleteFromDB(context);
				retMsg = "success";
				db.commit(transaction);
			}
		} catch (SessionException e) {
			db.cancel(transaction);
			retMsg = "error: " + e.getMsg();
		}
		Stats.saveAction(context, user, Stats.Action.DeleteUser, retMsg);
		response.getWriter().print(retMsg);
	}

}
