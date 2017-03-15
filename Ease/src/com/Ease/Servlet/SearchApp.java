package com.Ease.Servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class SearchApp
 */
@WebServlet("/SearchApp")
public class SearchApp extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchApp() {
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
		try {
			sm.needToBeConnected();
			String search = sm.getServletParam("search", true);
			String tags = sm.getServletParam("tags", true);
			if (search == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty search.");
			if (tags == null)
				throw new GeneralException(ServletManager.Code.ClientWarning, "Empty tags.");
			JSONParser parser = new JSONParser();
			Object array = parser.parse(StringEscapeUtils.unescapeHtml4(tags));
			JSONArray tagsArray = (JSONArray) array;
			JSONArray result = ((Catalog)sm.getContextAttr("catalog")).search(search, tagsArray);
			sm.setResponse(ServletManager.Code.Success, result.toJSONString());
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
