package com.Ease.servlet;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.context.DataBase;
import com.Ease.session.Account;
import com.Ease.session.App;
import com.Ease.session.LogWith;
import com.Ease.session.User;
import com.Ease.stats.Stats;


/**
 * Servlet implementation class AskInfo
 */

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
		// TODO Auto-generated method stub
		String retMsg;
		HttpSession session = request.getSession();
		User user = null;
		
		try {
			int profileIndex = Integer.parseInt(request.getParameter("profileIndex"));
			int appIndex = Integer.parseInt(request.getParameter("appIndex"));
			DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");

			user = (User)(session.getAttribute("User"));
			if (user == null) {
				RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
				rd.forward(request, response);
				return ;
			} else if (profileIndex < 0 || profileIndex >= user.getProfiles().size()) {
				retMsg = "error: Bad profile's index.";
			} else if (appIndex < 0 || appIndex >= user.getProfiles().get(profileIndex).getApps().size()) {
				retMsg = "error: Bad website's index.";
			} else {
				App app = user.getProfiles().get(profileIndex).getApps().get(appIndex);
				boolean again = true;
				JSONArray ja = new JSONArray();
				while (again){
					JSONObject obj = new JSONObject();
					if (app.getType().equals("Account")) {
						JSONObject appUser = new JSONObject();
						appUser.put("login", ((Account)app).getLogin());
						appUser.put("password", ((Account)app).getPassword());
						obj.put("user", appUser);
					} else if (app.getType().equals("LogWith")) {
						obj.put("logWith", ((LogWith)app).getAccount(user).getSite().getName());
					}
					JSONParser parser = new JSONParser();
					JSONObject a = (JSONObject) parser.parse(new FileReader(session.getServletContext().getRealPath(app.getSite().getFolder() + "connect.json")));
					obj.put("website", a);
					ja.add(0, obj);
					if (app.getType().equals("Account")) {
						again = false;
					} else {
						app = ((LogWith)app).getAccount(user);
					}
				}
				retMsg = "succes: " + ja.toString();
			}
		} catch (NumberFormatException e) {
			retMsg = "error: Bad index";
		} catch (ParseException e) {
			retMsg = "error: " + e.getMessage();
		}
		response.getWriter().print(retMsg);
	}

}
