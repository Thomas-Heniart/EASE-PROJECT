package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.data.Hashing;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditUserEmail
 */
public class EditUserEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserEmail() {
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
	
		String email = request.getParameter("email");
		String oldPassword = request.getParameter("password");

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
			}else if (oldPassword == null || !user.getPassword().equals(Hashing.SHA(oldPassword, user.getSaltEase()))){
				retMsg = "error: Wrong current password.";
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to access data base.";
			} else if (email == null || Regex.isEmail(email) == false) {
				retMsg = "error: Bad email.";
			} else {
				ResultSet rs;
				if ((rs = db.get("SELECT * FROM users WHERE email='" + email + "' AND user_id!=" + user.getId() + " LIMIT 0, 1;")) == null) {
					retMsg = "error: Can't access data base.";
				} else if (rs.next()) {
					retMsg = "error: Email already used.";
				} else {
					user.setEmail(email);
					user.updateInDB(session.getServletContext());
					retMsg = "success";
				}
			}
		} catch (SessionException e) {
			retMsg = "error: " + e.getMsg();
		} catch (SQLException e) {
			retMsg = "error: Can't access data base.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditUser, retMsg);
		response.getWriter().print(retMsg);
	}

}
