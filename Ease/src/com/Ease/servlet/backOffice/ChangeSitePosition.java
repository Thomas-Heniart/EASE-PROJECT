package com.Ease.servlet.backOffice;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.context.DataBase;
import com.Ease.context.Site;
import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

/**
 * Servlet implementation class ChangeSitePosition
 */
@WebServlet("/ChangeSitePosition")
public class ChangeSitePosition extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeSitePosition() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		DataBase db = (DataBase) session.getServletContext().getAttribute("DataBase");
		ServletItem SI = new ServletItem(ServletItem.Type.ChangeSitePosition, request, response, user);
		if (db.connect() != 0) {
			SI.setResponse(ServletItem.Code.DatabaseNotConnected, "Failed to connect to db");
			SI.sendResponse();
		}
		if (user == null || !user.isAdmin(session.getServletContext())) {
			RequestDispatcher rd = request.getRequestDispatcher("admin.jsp");
			rd.forward(request, response);
		}
		String siteId = SI.getServletParam("siteId");
		SiteManager siteManager = (SiteManager) session.getAttribute("siteManager");
		try {
			db.set("CALL lockWebsite(" + siteId + ");");
			siteManager.refresh(db);
			SI.setResponse(200, "Sites positions updated");
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, "SQL error");
			e.printStackTrace();
		}
		SI.sendResponse();
	}

}
