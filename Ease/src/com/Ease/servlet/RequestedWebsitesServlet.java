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


import com.Ease.context.DataBase;

/**
 * Servlet implementation class AskInfo
 */

public class RequestedWebsitesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RequestedWebsitesServlet() {
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
		// TODO Auto-generated method stub
		String retMsg;
		HttpSession session = request.getSession();


		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		ResultSet rs = null;

		if((rs = db.get("select * from askForSite;"))==null){
			retMsg = "error: Could not connect to database";
		} else {
			retMsg = "success:";
			try {
				while(rs.next()){
					retMsg+=";"+rs.getString(rs.findColumn("site"))+"-SENTBY-"+rs.getString(rs.findColumn("email"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				retMsg = "error: Problem with database";
			}
		}

		response.getWriter().print(retMsg);
	}

}
