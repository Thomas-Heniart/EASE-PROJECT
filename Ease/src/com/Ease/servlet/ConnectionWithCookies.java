package com.Ease.servlet;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.annotation.WebServlet;

import com.Ease.context.DataBase;
import com.Ease.data.Hashing;
import com.Ease.data.ServletItem;
import com.Ease.session.SessionException;
import com.Ease.session.SessionSave;
import com.Ease.session.SessionSave.SessionSaveData;
import com.Ease.session.User;

/**
 * Servlet implementation class ConnectionServlet
 */
@WebServlet("/connectionWithCookies")
public class ConnectionWithCookies extends HttpServlet {
       
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConnectionWithCookies() {
        super();
    }
 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		User user = (User)(session.getAttribute("User"));
		ServletItem SI = new ServletItem(ServletItem.Type.ConnectionServlet, request, response, user);
		
		// Get Parameters
		String sessionId = request.getParameter("sessionId");
		String token = request.getParameter("token");
		// --
		
		DataBase db = (DataBase)session.getServletContext().getAttribute("DataBase");
		
		try {
			if(user != null){
				SI.setResponse(ServletItem.Code.BadParameters,"A user is already connected.");
			}else if (db.connect() != 0){
				SI.setResponse(ServletItem.Code.DatabaseNotConnected, "There is a problem with our Database, please retry in few minutes.");
			} else if (sessionId == null){
				SI.setResponse(ServletItem.Code.BadParameters, "Wrong session id.");
			} else if (token == null) {
				SI.setResponse(ServletItem.Code.BadParameters, "Wrong token.");
			} else {		
				ResultSet rs;
				if ((rs = db.get("select * from savedSessions where sessionId = '" + sessionId + "';")) == null) {
					SI.setResponse(ServletItem.Code.LogicError, "Impossible to access data base.");
				} else if (rs.next()){
					String saltToken = rs.getString(SessionSaveData.SALTTOKEN.ordinal());
					String hashedToken = Hashing.SHA(token, saltToken);
					if (rs.getString(SessionSaveData.TOKEN.ordinal()).equals(hashedToken)){
						SessionSave sessionSave = new SessionSave(rs, token, session.getServletContext());
						if ((rs = db.get("select * from users where user_id = '" + sessionSave.getUserId() + "';")) == null) {
							SI.setResponse(ServletItem.Code.LogicError, "Impossible to access data base.");
						} else if (rs.next()){
							user = new User(rs, sessionSave.getKeyUser(), sessionSave.getUserId(), session.getServletContext());
							session.setAttribute("User", user);
							session.setAttribute("SessionSave", sessionSave);
							SI.setResponse(200, "Connected with cookies.");
						}
						
					} else {
						SI.setResponse(199, "Wrong session token.");
					}
				} else {
					SI.setResponse(199, "Wrong session id.");
				}
			}
		} catch (SessionException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, e.getStackTrace().toString());
		}
		SI.sendResponse();
		
		
		Cookie 	cookie = null;
		Cookie 	cookies[] = request.getCookies();
		if (cookies != null && SI.getCode() != 200){
			for (int i = 0;i < cookies.length ; i++) {
				cookie = cookies[i];
				if((cookie.getName()).compareTo("sId") == 0){
					cookie.setValue("");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				} else if((cookie.getName()).compareTo("sTk") == 0){
					cookie.setValue("");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}