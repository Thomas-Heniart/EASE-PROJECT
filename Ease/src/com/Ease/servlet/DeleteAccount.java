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
import com.Ease.data.ServletItem;
import com.Ease.session.User;

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
		User user = (User) (session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.SendVerificationEmail, request, response, user);
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		if (user == null) {
			SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			SI.sendResponse();
		}
		if (db.connect() != 0) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected,
					"There is a problem with our Database, please retry in few minutes.");
			SI.sendResponse();
		}
		String password = request.getParameter("password");
		//TODO
	}

}
