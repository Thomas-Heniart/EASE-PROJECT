package com.Ease.servlet;

import java.io.FileReader;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.Ease.data.ServletItem;
import com.Ease.session.App;
import com.Ease.session.ClassicAccount;
import com.Ease.session.LogWithAccount;
import com.Ease.session.User;


/**
 * Servlet implementation class AskInfo
 */
@WebServlet("/askInfo")
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
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.AskInfo, request, response, user);
		
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
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
	}
}
