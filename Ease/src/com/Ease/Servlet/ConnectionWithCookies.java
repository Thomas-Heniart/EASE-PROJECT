package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.SessionSave;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.websocket.WebsocketMessage;
import com.Ease.websocket.WebsocketSession;
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
		User user = (User) (session.getAttribute("user"));
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);

		String sessionId = sm.getServletParam("sessionId", false);
		String token = sm.getServletParam("token",false);
		String socketId = sm.getServletParam("socketId", true);
		// --
		@SuppressWarnings("unchecked")
		Map<String, WebsocketSession> sessionWebsockets = (Map<String, WebsocketSession>)session.getAttribute("sessionWebsockets");

		boolean success = false;
		try{
			if(user != null){
				sm.setResponse(ServletManager.Code.ClientError,"An user is already connected.");
			} else if (sessionId == null){
				sm.setResponse(ServletManager.Code.ClientWarning, "Wrong session id.");
			} else if (token == null) {
				sm.setResponse(ServletManager.Code.ClientWarning, "Wrong token.");
			} else {
				SessionSave sessionSave = SessionSave.loadSessionSave(sessionId, token, sm);
				user = User.loadUserFromCookies(sessionSave, sm);
				session.setAttribute("user", user);
				//sm.setResponse(ServletManager.Code.Success, "Connected with cookies.");
				sm.redirect("index.jsp");
				sm.setSocketId(socketId);
				sm.addWebsockets(sessionWebsockets);
				user.putAllSockets(sessionWebsockets);
				sm.addToSocket(WebsocketMessage.connectionMessage());
				success = true;
			}
		} catch (GeneralException e){
			sm.setResponse(e);
		}
		sm.sendResponse();


		Cookie 	cookie = null;
		Cookie 	cookies[] = request.getCookies();
		if (cookies != null && !success){
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

		//RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
		//rd.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
}