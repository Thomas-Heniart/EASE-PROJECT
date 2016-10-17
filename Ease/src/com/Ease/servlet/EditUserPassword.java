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
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.User;


/**
 * Servlet implementation class EditUserPassword
 */
@WebServlet("/editUserPassword")
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
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.EditUserPassword, request, response, user);
		
		// Get Parameters
		String password = request.getParameter("password");
		String confirmPassword = request.getParameter("confirmPassword");
		String oldPassword = request.getParameter("oldPassword");
		// --

		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		String hashedPassword;
		try {
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (oldPassword == null || !user.getHashedPassword().equals(Hashing.SHA(oldPassword, user.getSaltEase()))){
				SI.setResponse(ServletItem.Code.BadParameters, "Wrong password.");
			} else if (password == null || Regex.isPassword(password) == false) {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad new password");
			} else if (confirmPassword == null || !confirmPassword.equals(password)) {
				SI.setResponse(ServletItem.Code.BadParameters, "Passwords are not the same.");
			} else if ((hashedPassword = Hashing.SHA(password, user.getSaltEase())) == null){
				SI.setResponse(ServletItem.Code.LogicError, "Can't hash password.");
			} else {
				user.setHashedPassword(hashedPassword);
				user.updateInDB(session.getServletContext(), password);
				SI.setResponse(200, "Password edited.");
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}
