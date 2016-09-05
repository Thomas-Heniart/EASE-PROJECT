package com.Ease.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.Regex;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.stats.Stats;

/**
 * Servlet implementation class EditUserPassword
 */
@WebServlet("/EditUserPassword")
public class EditUserPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserPassword() {
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
		
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String oldPassword = request.getParameter("oldPassword");
		
		HttpSession session = request.getSession();
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		User user = (User)(session.getAttribute("User"));
		String hashedPassword;
		String retMsg;
		try {
			if (user == null) {
				Stats.saveAction(session.getServletContext(), user, Stats.Action.EditUser, "");
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (db.connect() != 0){
				retMsg = "error: Impossible to access data base.";
			} else if (oldPassword == null || !user.getPassword().equals(Hashing.SHA(oldPassword, user.getSaltEase()))){
				retMsg = "error: Wrong current password.";
			} else if (password == null || Regex.isPassword(password) == false) {
				retMsg = "error: This password does not respect safety rules.";
			} else if (confirmPassword == null || !confirmPassword.equals(password)) {
				retMsg = "error: Confirmation password does not match.";
			} else if ((hashedPassword = Hashing.SHA(password, user.getSaltEase())) == null){
				retMsg = "error: Can't hash password.";
			} else {
				user.setPassword(hashedPassword);
				user.updateInDB(session.getServletContext(), password);
				retMsg = "success";
			}
		} catch (SessionException e) {
			retMsg = "error: " + e.getMsg();
		}
		Stats.saveAction(session.getServletContext(), user, Stats.Action.EditUser, retMsg);
		response.getWriter().print(retMsg);
	}
}
