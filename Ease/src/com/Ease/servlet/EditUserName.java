package com.Ease.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditUserName
 */
public class EditUserName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserName() {
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
		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		User user = (User)(session.getAttribute("User"));
		String retMsg;
		try {
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditUser, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to access data base.";
			} else if (fname == null || fname == "") {
				retMsg = "error: Bad first name.";
			} else if (lname == null || lname == "") {
				retMsg = "error: Bad last name.";
			} else {
				user.setFirstName(fname);
				user.setLastName(lname);
				user.updateInDB(session.getServletContext());
				retMsg = "success";
			}
		} catch (SessionException e) {
			retMsg = "error: " + e.getMsg();
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditUser, retMsg);
		response.getWriter().print(retMsg);
	}

}
