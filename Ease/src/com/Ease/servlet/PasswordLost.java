package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Regex;
import com.Ease.session.User;

/**
 * Servlet implementation class PasswordLost
 */
@WebServlet("/PasswordLost")
public class PasswordLost extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PasswordLost() {
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
		HttpSession session = request.getSession();
		User user = null;
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		String email = request.getParameter("email");
		
		try {
			user = (User)(session.getAttribute("User"));
			if (user != null) {
				retMsg = "You are logged to Ease.";
			} else if (email == null || Regex.isEmail(email) == false) {
				retMsg = "Bad email.";
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to connect data base.";
			} else {
				ResultSet rs = db.get("select * from users where email='" + email + "';");
				if (rs.next()) {
					String linkCode = "";
					Random r = new Random();
					String			alphabet = "azertyuiopqsdfghjklwxcvbnm1234567890AZERTYUIOPQSDFGHJKLMWXCVBN";
					for (int i = 0;i < 126 ; ++i) {
						linkCode += alphabet.charAt(r.nextInt(alphabet.length()));			
					}
					db.set("insert into PasswordLost values (" + email + ", " + linkCode + ");");
					retMsg= "succes";
				} else {
					retMsg = "error: This email is not associate with an account.";
				}
			}
		} catch (SQLException e) {
			retMsg = "error:" + e.getMessage();
			e.printStackTrace();
		}
		response.getWriter().print(retMsg);
	}

}
