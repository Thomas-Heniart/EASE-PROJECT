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
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.session.User.UserData;

/**
 * Servlet implementation class ConnectionServlet
 */
@WebServlet("/connection")
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
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.ConnectionServlet, request, response, user);
		
		// Get Parameters
		String email = SI.getServletParam("email");
		String password = request.getParameter("password");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		if (user != null){
			session.setAttribute("User", null);
		}
		
		try {
			if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (email == null || Regex.isEmail(email) == false){
				SI.setResponse(ServletItem.Code.BadParameters, "Incorrect email.");
			} else if (password == null || Regex.isPassword(password) == false) {
				SI.setResponse(ServletItem.Code.BadParameters, "Incorrect password");
			} else {		
				ResultSet rs;
				if ((rs = db.get("select * from users where email = '" + email + "';")) == null) {
					SI.setResponse(ServletItem.Code.LogicError, "Impossible to access data base.");
				} else if (rs.next()){
					String saltEase = rs.getString(UserData.SALTEASE.ordinal());
					String hashedPass = Hashing.SHA(password, saltEase);
					if (rs.getString(UserData.PASSWORD.ordinal()).equals(hashedPass)){
						user = new User(rs, password, session.getServletContext());
						session.setAttribute("User", user);
						SI.setResponse(200, "Connected.");
					} else {
						SI.setResponse(199, "Wrong login or password.");
					}
				} else {
					SI.setResponse(199, "Wrong login or password.");
				}
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}