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
import com.Ease.data.Hashing;
import com.Ease.data.Regex;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.session.User.UserData;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class ConnectionServlet
 */
public class ConnectionServlet extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String			email = request.getParameter("email");
		String			password = request.getParameter("password");
		
		String			retMsg;
		
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		if (session.getAttribute("User") != null){
			RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
			rd.forward(request, response);
			return ;
		}
		
		User user = null;
		
		try {
			if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else if (email == null || Regex.isEmail(email) == false){
				 retMsg = "error: Bad email";
			} else if (password == null || Regex.isPassword(password) == false) {
				retMsg = "error: Bad password.";
			} else {		
				ResultSet rs;
				if ((rs = db.get("select * from users where email = '" + email + "';")) == null) {
					retMsg = "error: Impossible to access data base.";
				} else if (rs.next()){
					String saltEase = rs.getString(UserData.SALTEASE.ordinal());
					String hashedPass = Hashing.SHA(password, saltEase);
					if (rs.getString(UserData.PASSWORD.ordinal()).equals(hashedPass)){
						user = new User(rs, password, session.getServletContext());
						session.setAttribute("User", user);
						retMsg = "success";
					} else {
						retMsg = "error: Wrong login or password.";
					}
				} else {
					retMsg = "error: Wrong login or password.";
				}
			}
		} catch (SessionException e) {
			retMsg = "error: " + e.getMsg();
			e.printStackTrace();
		} catch (SQLException e) {
			retMsg = "error: Impossible to access data base.";
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.Connect, retMsg);
		response.getWriter().print(retMsg);
	}
}