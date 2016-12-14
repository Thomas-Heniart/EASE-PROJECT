package com.Ease.Servlet.App;

import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import org.json.simple.parser.ParseException;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Catalog.Website;
import com.Ease.Dashboard.App.WebsiteApp.ClassicApp.ClassicApp;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

/**
 * Servlet implementation class AddClassicApp
 */
@WebServlet("/AskInfo")
public class AskInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AskInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		/*HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AskInfo, request, response, user);
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		// Get Parameters
		String appIdParam = SI.getServletParam("appId");
		// --
		
		try {
			int appId = Integer.parseInt(appIdParam);

			if (user == null) {
				SI.setResponse(ServletItem.Code.NotConnected, "You are not connected.");
			} else {
				App app = null;
				if ((app = user.getApp(appId)) == null)
					SI.setResponse(ServletItem.Code.LogicError, "No appId.");
				else if (app.isEmpty() == true)
					SI.setResponse(ServletItem.Code.LogicError, "This is an empty app.");
				else {
					JSONObject classicAccountObject = new JSONObject();					
					boolean again = true;
					JSONArray ja = new JSONArray();
					while (again){
						JSONObject obj = new JSONObject();
						if (app.getType().equals("ClassicAccount")) {
							ResultSet accountInformationsRs = db.get("SELECT information_name, information_value FROM ClassicAccountsInformations WHERE account_id=" + app.getAccount().getId() + ";");
							while (accountInformationsRs.next()) {
								String information_name = accountInformationsRs.getString(1);
								String information_value = accountInformationsRs.getString(2);
								com if (information_name.equals("password")){
									classicAccountObject.put(information_name, app.getAccount().getPassword());
								} else com
								classicAccountObject.put(information_name, ((ClassicAccount)app.getAccount()).getInfo(information_name));
							}
							obj.put("user", classicAccountObject);
						} else if (app.getType().equals("LogWithAccount")) {
							obj.put("logWith", ((LogWithAccount)app.getAccount()).getLogWithApp(user).getSite().getName());
						}
						JSONParser parser = new JSONParser();
						JSONObject a = (JSONObject) parser.parse(new FileReader(session.getServletContext().getRealPath(app.getSite().getFolder() + "connect.json")));
						a.put("loginUrl", app.getSite().getUrl());
						a.put("siteId", app.getSite().getId());
						a.put("siteSrc", app.getSite().getFolder());
						obj.put("website", a);
						ja.add(0, obj);
						if (app.getType().equals("ClassicAccount")) {
							again = false;
						} else {
							app = ((LogWithAccount)app.getAccount()).getLogWithApp(user);
						}
					}
					SI.setResponse(200, ja.toString());
				}
			}
		} catch (NumberFormatException e) {
			SI.setResponse(ServletItem.Code.BadParameters, "Numbers exception.");
		} catch (ParseException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, "SQL error");
			e.printStackTrace();
		}
		SI.sendResponse();
	}*/
	
		HttpSession session = request.getSession();
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		
		try {
			sm.needToBeConnected();
			String appId = sm.getServletParam("appId", true);
			if (appId == null || appId.isEmpty())
				throw new GeneralException(ServletManager.Code.ClientError, "Wrong appId.");
			user.moveApp(Integer.parseInt(appId), Integer.parseInt(profileId), Integer.parseInt(position), sm);
			sm.setResponse(ServletManager.Code.Success, "App moved.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (NumberFormatException e) {
			sm.setResponse(ServletManager.Code.ClientError, "Wrong numbers.");
		}
		sm.sendResponse();
	}
}