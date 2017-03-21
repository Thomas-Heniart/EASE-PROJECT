package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DatabaseRequest;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class BlacklistWebsite
 */
@WebServlet("/BlacklistWebsite")
public class BlacklistWebsite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlacklistWebsite() {
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
			String url = sm.getServletParam("url", true);
			if (url == null || url.equals(""))
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty url");
			DatabaseRequest db_request = sm.getDB().prepareRequest("INSERT INTO blacklistedWebsites values(?, ?);");
			db_request.setNull();
			db_request.setString(url);
			sm.setResponse(ServletManager.Code.Success, "Website added to blacklist");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
