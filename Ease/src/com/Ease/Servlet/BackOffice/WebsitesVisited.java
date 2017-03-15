package com.Ease.Servlet.BackOffice;

import java.io.IOException;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.WebsitesVisitedManager;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class WebsitesVisited
 */
@WebServlet("/WebsitesVisited")
public class WebsitesVisited extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public WebsitesVisited() {
        super();
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
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		Catalog catalog = (Catalog) sm.getContextAttr("catalog");
		DataBaseConnection db = sm.getDB();
		try {
			WebsitesVisitedManager websitesVisitedManager = (WebsitesVisitedManager) sm.getContextAttr("websitesVisitedManager");
			String jsonString = sm.getServletParam("websitesVisited", true);
			if (jsonString == null || jsonString.equals(""))
				throw new GeneralException(ServletManager.Code.ClientError, "websitesVisited is null");
			JSONParser parser = new JSONParser();
			try {
				System.out.println(jsonString);
				JSONObject websitesVisited = (JSONObject)parser.parse(StringEscapeUtils.unescapeHtml4(jsonString));
				for (Object obj : websitesVisited.entrySet()) {
					Entry<Object, Object> entry = (Entry<Object, Object>) obj;
					String url = (String)entry.getKey();
					String count = (String)entry.getValue();
					websitesVisitedManager.addWebsiteRequest(url, Integer.valueOf(count), db, catalog);
					sm.setResponse(ServletManager.Code.Success, "Websites visited added");
				}
			} catch (ParseException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		} catch (GeneralException e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
