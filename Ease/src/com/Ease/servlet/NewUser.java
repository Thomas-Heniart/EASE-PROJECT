package com.Ease.servlet;
import java.io.FileWriter;
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
import com.Ease.session.Profile;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;


/**
 * Servlet implementation class NewUser
 */
public class NewUser extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * @see HttpServlet#HttpServlet()
     */
    public NewUser() {
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
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		
		String retMsg;
		User user = null;
		
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		if (db.connect() != 0){
			retMsg = "error: Can't connect data base.";
		} else if (fname == null || fname.length() < 2){
			retMsg = "error: Bad first name.";	
		} else if (lname == null || lname.length() < 2){
			retMsg = "error: Bad last name.";
		} else if (email == null || Regex.isEmail(email) == false){
			retMsg = "error: Bad email.";
		} else if (password == null || Regex.isPassword(password) == false) {
			retMsg = "error: Bad password.";
		} else if (confirmPassword == null || password.equals(confirmPassword) == false) {
			retMsg = "error: Bad confirm password.";
		} else {
			try {
				ResultSet rs;
				if ((rs = db.get("select * from users where email = '" + email + "' limit 0, 1;")) == null) {
					retMsg = "error: Can't get data from data base.";
				} else if (rs.next()) {
					retMsg = "error: Email already used.";
				} else {
					user = new User(fname, lname, email, "0606060606", password, session.getServletContext());
					Profile profile = new Profile("Perso", "#ff974f", "", user, null, session.getServletContext());
					user.addProfile(profile);
					retMsg = "success";
					FileWriter fw = new FileWriter("users.txt", true);
					fw.write(fname + ", " + lname + ", " + email + ", " + password + "\n");
					fw.close();
				}
			} catch (SessionException e) {
				retMsg = "error: " + e.getMsg();
			} catch (SQLException e) {
				retMsg = "error: Can't access data base.";
			}
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.NewUser, retMsg);
		response.getWriter().print(retMsg);
	}
}