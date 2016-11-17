package com.Ease.servlet;

import java.io.IOException;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.context.SiteManager;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.User;
import com.Ease.session.update.ClassicUpdate;
import com.Ease.session.update.LogwithUpdate;

/**
 * Servlet implementation class StockUpdate
 */
@WebServlet("/StockUpdate")
public class StockUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StockUpdate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.StockUpdate, request, response, user);
		
		String updatesJson = SI.getServletParam("updates");
		
		JSONParser parser = new JSONParser();
		try {
			JSONObject updatesObj = (JSONObject) parser.parse(updatesJson);
			JSONArray updatesList = (JSONArray) updatesObj.get("updates");
			Iterator<JSONObject> iterator = updatesList.iterator();
			SiteManager	sm = (SiteManager) session.getServletContext().getAttribute("siteManager");
			while (iterator.hasNext()) {
				JSONObject update = iterator.next();
				String siteId;
				if ((siteId = sm.haveThisUrl(update.get("url").toString())) != null) {
					//Le site est dans le catalog
					String logwithString;
					if ((logwithString = update.get("logwith").toString()) == null) {
						// Classic account
						user.getUpdates().add(ClassicUpdate.CreateClassicUpdate(siteId, update.get("login").toString(), update.get("pass").toString(), user, session.getServletContext()));
					} else {
						// logWith account
						user.getUpdates().add(LogwithUpdate.CreateLogwithUpdate(siteId, update.get("login").toString(), Integer.parseInt(logwithString), user, session.getServletContext()));
					}
				} else {
					// L'update n'est pas dans le catalog
					
				}
			}
			SI.setResponse(200, "Updates uploaded.");
		} catch (ParseException | NumberFormatException | SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, "Update data are corrupted.");
		}
		SI.sendResponse();
	}
}
