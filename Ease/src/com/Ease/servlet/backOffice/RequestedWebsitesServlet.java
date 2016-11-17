package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class AskInfo
 */
@WebServlet("/requestedWebsites")
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
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.RequestedWebsitesServlet, request, response, user);
		
		// Get Parameters
		
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		ResultSet rs = null;
		try {
			rs = db.get("select * from askForSite;");
			String retMsg = "";
			while(rs.next()){
				retMsg += ";" + rs.getString(rs.findColumn("site")) + "-SENTBY-" + rs.getString(rs.findColumn("email"));
			}
			SI.setResponse(200, retMsg);
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		}
		SI.sendResponse();
	}
}
