package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.WebsitesVisitedManager;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class WhitelistWebsite
 */
@WebServlet("/WhitelistWebsite")
public class WhitelistWebsite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WhitelistWebsite() {
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		User user = sm.getUser();
		try {
			sm.needToBeConnected();
			if (!user.isAdmin())
				throw new GeneralException(ServletManager.Code.ClientError, "You ain't admin");
			String single_id = sm.getServletParam("single_id", true);
			String websiteDone = sm.getServletParam("isInCatalog", true);
			if (single_id == null || single_id.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty single_id");
			if (websiteDone == null || websiteDone.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty isDone");
			boolean isInCatalog = Boolean.parseBoolean(websiteDone);
			if (!isInCatalog) {
				WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) sm.getContextAttr("websitesVisitedManager");
				websitesVisitedManager.sendBlacklistedWebsiteToWebsitesVisitedWithSingleId(Integer.parseInt(single_id), sm);
			} else {
				Catalog catalog = (Catalog) sm.getContextAttr("catalog");
				catalog.whitelistWebsiteWithSingleId(Integer.parseInt(single_id), sm);
			}
			sm.setResponse(ServletManager.Code.Success, "Website added to websites visited");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
