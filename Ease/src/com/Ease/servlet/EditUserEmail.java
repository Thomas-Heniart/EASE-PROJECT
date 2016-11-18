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
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.data.Hashing;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class EditUserEmail
 */
@WebServlet("/editUserEmail")
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
	
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.EditUserEmail, request, response, user);
		
		// Get Parameters
		String email = SI.getServletParam("email");
		String oldPassword = SI.getServletParam("password");
		// --
				
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		
		try {
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			}else if (oldPassword == null || !user.getHashedPassword().equals(Hashing.SHA(oldPassword, user.getSaltEase()))){
				SI.setResponse(ServletItem.Code.BadParameters, "Wrong password.");
			} else if (email == null || Regex.isEmail(email) == false) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad email.");
			} else {
				ResultSet rs;
				if ((rs = db.get("SELECT * FROM users WHERE email='" + email + "' AND user_id!=" + user.getId() + " LIMIT 0, 1;")) == null) {
					SI.setResponse(ServletItem.Code.LogicError, "DB");
				} else if (rs.next()) {
					SI.setResponse(ServletItem.Code.BadParameters, "This email is already used.");
				} else {
					user.setEmail(email);
					user.updateInDB(session.getServletContext());
					SI.setResponse(200, "Email edited.");
				}
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		}
		SI.sendResponse();
	}

}
