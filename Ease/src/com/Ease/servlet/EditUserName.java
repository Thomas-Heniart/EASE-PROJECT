package com.Ease.servlet;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.User;

/**
 * Servlet implementation class EditUserName
 */
@WebServlet("/editUserName")
public class EditUserName extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUserName() {
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
		ServletItem SI = new ServletItem(ServletItem.Type.EditUserName, request, response, user);
		
		// Get Parameters
		String fname = SI.getServletParam("fname");
		String lname = SI.getServletParam("lname");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		try {
			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (fname == null || fname == "") {
				SI.setResponse(ServletItem.Code.BadParameters, "Bad first name.");
			} else {
				user.setFirstName(fname);
				if (lname != null && lname != "")
					user.setLastName(lname);
				user.updateInDB(session.getServletContext());
				SI.setResponse(200, "Name changed.");
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}
