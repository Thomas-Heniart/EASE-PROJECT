package com.Ease.Servlet;

//************************
//************************
//**** NOT FINISHED ******
//************************
//************************


import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Context.Group.GroupProfile;
import com.Ease.Context.Group.ProfilePermissions;
import com.Ease.Dashboard.App.ClassicApp;
import com.Ease.Dashboard.App.Website;
import com.Ease.Dashboard.Profile.Profile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
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
		User user = (User) (session.getAttribute("User"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		DataBaseConnection db = sm.getDB();
		
		String sessionId = sm.getServletParam("sessionId", false);
		String token = sm.getServletParam("token",false);
		
		try {
			if(user != null){
				sm.setResponse(ServletManager.Code.ClientError,"An user is already connected.");
			} else if (sessionId == null){
				sm.setResponse(ServletManager.Code.ClientWarning, "Wrong session id.");
			} else if (token == null) {
				sm.setResponse(ServletManager.Code.ClientWarning, "Wrong token.");
			} else {
				ResultSet rs = db.get("select * from savedSessions where sessionId = '" + sessionId + "';");
				if (rs.next()){
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
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
		} catch (SQLException e) {
			SI.setResponse(ServletItem.Code.LogicError, ServletItem.getExceptionTrace(e));
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