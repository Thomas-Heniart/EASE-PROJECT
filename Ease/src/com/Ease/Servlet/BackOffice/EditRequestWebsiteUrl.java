package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class EditRequestWebsiteUrl
 */
@WebServlet("/EditRequestWebsiteUrl")
public class EditRequestWebsiteUrl extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditRequestWebsiteUrl() {
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
		DataBaseConnection db = sm.getDB();
		Catalog catalog = (Catalog)sm.getContextAttr("catalog");
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "Not an admin");
			String newUrl = sm.getServletParam("newUrl", true);
			String oldUrl = sm.getServletParam("oldUrl", true);
			if (newUrl == null || newUrl.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Input is empty");
			if (oldUrl == null || oldUrl.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty url");
			db.set("UPDATE requestedWebsites SET site = '" + newUrl + "' WHERE site='" + oldUrl + "'");
			String retMsg = (catalog.getWebsiteWithHost(newUrl) != null) ? "integrated" : "pending";
			sm.setResponse(ServletManager.Code.Success, retMsg);
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
