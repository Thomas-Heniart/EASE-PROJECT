package com.Ease.Servlet.Update;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class FilterUpdate
 */
@WebServlet("/FilterUpdate")
public class FilterUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FilterUpdate() {
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
		
		try {
			String sessionId = sm.getServletParam("sessionId", true);
			String extensionId = sm.getServletParam("extensionId", true);
			String scrap = sm.getServletParam("scrap", true);
			if (sessionId == null || (user = ((Map<String, User>)sm.getContextAttr("sIdUserMap")).get(sessionId)) == null) {
				throw new GeneralException(ServletManager.Code.ClientError, "You are not connected.");
			} else if (extensionId == null || user.getExtensionKeys().haveThisKey(extensionId) == false) {
				throw new GeneralException(ServletManager.Code.ClientError, "This is not a private extension.");
			} else if (scrap == null) {
				throw new GeneralException(ServletManager.Code.ClientError, "Wrong scrap.");
			}
			JSONParser parser = new JSONParser();
			Object temp = parser.parse(scrap);
			JSONArray scrappedJson = (JSONArray) temp;
			String resp = "";
			Integer i = 0;
			for (Object obj : scrappedJson) {
				if (user.getUpdateManager().addUpdateFromJsonDeconnected(((JSONObject)obj).toString(), sm)) {
					resp += i.toString() + " ";
				}
				i++;
			}
			sm.setResponse(ServletManager.Code.Success, resp);
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (NumberFormatException e) {
			sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

}
