package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.AES;
import com.Ease.data.Hashing;
import com.Ease.data.Regex;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class ResetUser
 */
@WebServlet("/ResetUser")
public class ResetUser extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ResetUser() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.ResetUser, request, response, user);
		
		// Get Parameters
		String linkCode = SI.getServletParam("linkCode");
		String password = SI.getServletParam("password");
		String confirmPassword = SI.getServletParam("confirmPassword");
		// --

		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			if (user != null) {
				SI.setResponse(ServletItem.Code.AlreadyConnected, "You are logged on Ease.");
			} else if (password == null || Regex.isPassword(password) == false) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad password.");
			} else if (confirmPassword == null || confirmPassword.equals(password) == false){
				SI.setResponse(ServletItem.Code.BadParameters, "Passwords are not the same.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else {
				ResultSet rs = db.get("select * from PasswordLost where linkCode='" + linkCode + "';");
				if (rs.next()) {
					String email = rs.getString(1);
					rs = db.get("select * from users where email='" + email + "';");
					String hashedPassword;
					if ((hashedPassword = Hashing.SHA(password, rs.getString(7))) == null){
						SI.setResponse(ServletItem.Code.LogicError, "Can't hash password.");
					} else {
						String id = rs.getString(1);
						String cryptedKeyUser = AES.encryptUserKey(rs.getString(9), password, rs.getString(8));
						db.set("UPDATE users SET `password`='"+ hashedPassword + "', `keyUser`='"+ cryptedKeyUser +"' WHERE `user_id`='"+ id + "';");
						rs = db.get("select * from profiles where user_id=" + id + ";");
						while (rs.next()) {
							String profileId = rs.getString(1);
							db.set("update apps set account_id=NULL where profile_id=" + profileId + ";");
						}
						db.set("delete from PasswordLost where email='" + email + "';");
						SI.setResponse(200, "User reseted.");
					}
				} else {
					SI.setResponse(ServletItem.Code.BadParameters, "This is a bad link code.");
				}
			}
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}
