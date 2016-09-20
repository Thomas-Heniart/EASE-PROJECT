package com.Ease.servlet;

import java.io.IOException;


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

public class EraseRequestedWebsiteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EraseRequestedWebsiteServlet() {
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
		
		String website = request.getParameter("toErase");
		String retMsg;
		HttpSession session = request.getSession();

		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

		if(db.set("DELETE FROM askForSite WHERE site='" + website + "';") != 0){
			retMsg = "error: could not connect to db";
		} else {
			retMsg = "success";
		}
		System.out.println(retMsg);
		response.getWriter().print(retMsg);
	}

}
