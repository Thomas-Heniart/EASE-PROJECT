package com.Ease.Servlet.BackOffice;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DatabaseResult;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class GetWebsitesVisited
 */
@WebServlet("/GetWebsitesVisited")
public class GetWebsitesVisited extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetWebsitesVisited() {
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
				throw new GeneralException(ServletManager.Code.ClientWarning, "You ain't admin");
			DatabaseResult rs = sm.getDB().prepareRequest("SELECT * FROM websitesVisited ORDER BY count").get();
			//WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) sm.getContextAttr("websitesVisitedManager");
			JSONArray res = new JSONArray();
			while(rs.next()) {
				JSONObject tmp = new JSONObject();
				tmp.put("url", rs.getString(2));
				tmp.put("count", rs.getString(3));
				res.add(tmp);
			}
			/*for (Entry<String, Integer> urlAndCount : websitesVisitedManager.getWeightedWebsitesVisited()) {
				JSONObject tmp = new JSONObject();
				tmp.put("url", urlAndCount.getKey());
				tmp.put("count", urlAndCount.getValue());
				res.add(tmp);
			}*/
			sm.setResponse(ServletManager.Code.Success, res.toString());
			sm.setLogResponse("GetWebsitesVisited done");
		} catch(GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}
}
