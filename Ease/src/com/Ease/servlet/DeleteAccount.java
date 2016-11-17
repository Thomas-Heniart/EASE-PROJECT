package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.session.User;
import com.Ease.session.User.UserData;

/**
 * Servlet implementation class DeleteAccount
 */
@WebServlet("/DeleteAccount")
public class DeleteAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteAccount() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.SendVerificationEmail, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			SI.sendResponse();
		}
		try {
			db.connect();
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
			return ;
		}
		String password = request.getParameter("password");
		ResultSet rs;
		
		try {
			rs = db.get("select * from users where email = '" + user.getEmail() + "';");
			if (rs == null) {
				SI.setResponse(ServletItem.Code.LogicError, "Impossible to access data base.");
				SI.sendResponse();
			}
			if (rs.next()) {
				String saltEase = rs.getString(UserData.SALTEASE.ordinal());
				String hashedPass = Hashing.SHA(password, saltEase);
				if (rs.getString(UserData.PASSWORD.ordinal()).equals(hashedPass)) {
					db.set("CALL deleteUser(" + user.getId() + ")");
					Cookie 	cookie = null;
					Cookie 	cookies[] = request.getCookies();
					if (cookies != null){
						for (int i = 0;i < cookies.length ; i++) {
							cookie = cookies[i];
							if((cookie.getName()).compareTo("sId") == 0){
								cookie.setValue("");
								cookie.setMaxAge(0);
								response.addCookie(cookie);
							} else if((cookie.getName()).compareTo("sTk") == 0){
								cookie.setValue("");
								cookie.setMaxAge(0);
								response.addCookie(cookie);
							}
						}
					}
					session.invalidate();
				} else {
					SI.setResponse(199, "Wrong login or password.");
					SI.sendResponse();
				}

			}
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
			e.printStackTrace();
			SI.sendResponse();
		}
		SI.setResponse(200, "Account deleted");
		SI.sendResponse();
	}

}
