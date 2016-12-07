package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class MoveProfile
 */
@WebServlet("/MoveProfile")
public class MoveProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MoveProfile() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		try {
			sm.needToBeConnected();
			String single_id = sm.getServletParam("single_id", true);
			String column = sm.getServletParam("column", true);
			String position = sm.getServletParam("position", true);
			if (single_id == null)
				throw new GeneralException(ServletManager.Code.ClientError, "Single_id is null");
			if (column == null)
				throw new GeneralException(ServletManager.Code.ClientError, "Column is null");
			if (position == null)
				throw new GeneralException(ServletManager.Code.ClientError, "Position is null");
			try {
				Profile profileToMove = user.getProfile(Integer.parseInt(single_id));
				int transaction = db.startTransaction();
				profileToMove.setColumnIdx(Integer.parseInt(column), sm);
				profileToMove.setPositionIdx(Integer.parseInt(position), sm);
				db.commitTransaction(transaction);
				sm.setResponse(ServletManager.Code.Success, "Profile moved.");
			} catch (NumberFormatException e) {
				throw new GeneralException(ServletManager.Code.ClientError, e);
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
