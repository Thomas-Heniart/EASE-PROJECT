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
		String linkCode = null;
		linkCode = request.getParameter("linkCode");
		if (linkCode == null) {
			RequestDispatcher rd = request.getRequestDispatcher("newPassword.jsp");
			rd.forward(request, response);
		} else {
			RequestDispatcher rd = request.getRequestDispatcher("newPassword.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		String linkCode = request.getParameter("linkCode");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		try {
			user = (User)(session.getAttribute("User"));
			if (user != null) {
				retMsg = "You are logged to Ease.";
			} else if (password == null || Regex.isPassword(password) == false) {
				retMsg = "error: Bad password.";
			} else if (confirmPassword == null || confirmPassword.equals(password) == false){
				retMsg = "error: Bad confirmPassword";
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else {
				ResultSet rs = db.get("select * from PasswordLost where linkCode='" + linkCode + "';");
				if (rs.next()) {
					String email = rs.getString(1);
					rs = db.get("select * from users where email='" + email + "';");
					String hashedPassword;
					if ((hashedPassword = Hashing.SHA(password, rs.getString(7))) == null){
						retMsg = "error: Can't hash password.";
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
						retMsg = "succes";
					}
				} else {
					retMsg = "error: Bad linkCode";
				}
			}
		} catch (SQLException e) {
			retMsg = "error:" + e.getMessage();
			e.printStackTrace();
		}
		response.getWriter().print(retMsg);
	}

}
