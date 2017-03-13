package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EraseRequestedWebsite
 */
@WebServlet("/EraseRequestedWebsite")
public class EraseRequestedWebsite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EraseRequestedWebsite() {
        super();
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
		User user = (User)(session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			DataBaseConnection db = sm.getDB();
			String websiteUrl = sm.getServletParam("websiteUrl", true);
			String email = sm.getServletParam("email", true);
			DatabaseRequest db_request = db.prepareRequest("SELECT id FROM users WHERE email = ?;");
			db_request.setString(email);
			DatabaseResult userIdRs = db_request.get();
			if (!userIdRs.next())
				throw new GeneralException(ServletManager.Code.ClientError, "This user does not exist");
			String userId = userIdRs.getString(1);
			db_request = db.prepareRequest("DELETE FROM requestedWebsites WHERE user_id = ? AND site = ?;");
			db_request.setInt(userId);
			db_request.setString(websiteUrl);
			sm.setResponse(ServletManager.Code.Success, "Deleted");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
