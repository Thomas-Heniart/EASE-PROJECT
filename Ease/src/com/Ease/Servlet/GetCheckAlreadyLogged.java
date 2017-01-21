package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetCheckAlreadyLogged
 */
@WebServlet("/GetCheckAlreadyLogged")
public class GetCheckAlreadyLogged extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetCheckAlreadyLogged() {
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

		String host = sm.getServletParam("host", true);
		
		try {
			if (host == null || host.equals("")) {
				throw new GeneralException(ServletManager.Code.ClientWarning, "Wrong host.");
			}
			Website site = ((Catalog)sm.getContextAttr("catalog")).getWebsiteWithHost(host);
			if (site == null) {
				sm.setResponse(ServletManager.Code.UserMiss, "No website found.");
			} else {
				JSONObject json = site.getJSON(sm);
				JSONArray checkAlreadyLogged = (JSONArray)json.get("checkAlreadyLogged");
				sm.setResponse(ServletManager.Code.Success, checkAlreadyLogged.toString());
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
