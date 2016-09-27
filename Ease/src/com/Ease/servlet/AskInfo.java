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
import com.Ease.session.App;
import com.Ease.session.ClassicAccount;
import com.Ease.session.LogWithAccount;
import com.Ease.session.User;


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
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			int profileIndex = Integer.parseInt(request.getParameter("profileIndex"));
			int appIndex = Integer.parseInt(request.getParameter("appIndex"));

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
				if (app.isEmpty() == true)
					retMsg = "error: This is an empty app";
				else {
					boolean again = true;
					JSONArray ja = new JSONArray();
					while (again){
						JSONObject obj = new JSONObject();
						if (app.getType().equals("ClassicAccount")) {
							JSONObject appUser = new JSONObject();
							appUser.put("login", ((ClassicAccount)app.getAccount()).getLogin());
							appUser.put("password", ((ClassicAccount)app.getAccount()).getPassword());
							obj.put("user", appUser);
						} else if (app.getType().equals("LogWithAccount")) {
							obj.put("logWith", ((LogWithAccount)app.getAccount()).getLogWithApp(user).getSite().getName());
						}
						JSONParser parser = new JSONParser();
						JSONObject a = (JSONObject) parser.parse(new FileReader(session.getServletContext().getRealPath(app.getSite().getFolder() + "connect.json")));
						a.put("loginUrl", app.getSite().getUrl());
						obj.put("website", a);
						ja.add(0, obj);
						if (app.getType().equals("ClassicAccount")) {
							again = false;
						} else {
							app =  ((LogWithAccount)app.getAccount()).getLogWithApp(user);
						}
					}
					retMsg = "succes: " + ja.toString();
				}
			}
		} catch (NumberFormatException e) {
			retMsg = "error: Bad index";
		} catch (ParseException e) {
			retMsg = "error: " + e.getMessage();
		}
		response.getWriter().print(retMsg);
	}

}
