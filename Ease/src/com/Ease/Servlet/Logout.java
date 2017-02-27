package com.Ease.Servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;
import com.Ease.websocket.WebsocketMessage;
import com.Ease.websocket.WebsocketSession;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/Logout")
public class Logout extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Logout() {
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
		HttpSession session = request.getSession();
		User user = (User)session.getAttribute("user");
		ServletManager sm = new ServletManager(this.getClass().getName(), request, response, true);
		try {
			logoutUser(user, sm);
			session.invalidate();
			sm.setResponse(ServletManager.Code.Success, "Logged out.");
		} catch (GeneralException e) {
			sm.setResponse(e);
		} catch (Exception e) {
			sm.setResponse(e);
		}
		sm.sendResponse();
	}

	public static void logoutUser(User user, ServletManager sm) throws GeneralException {
		sm.needToBeConnected();
		user.getSessionSave().eraseFromDB(sm);
		Cookie 	cookie = null;
		Cookie 	cookies[] = sm.getRequest().getCookies();
		if (cookies != null){
			for (int i = 0;i < cookies.length ; i++) {
				cookie = cookies[i];
				if((cookie.getName()).compareTo("sId") == 0){
					cookie.setValue("");
					cookie.setMaxAge(0);
					sm.getResponse().addCookie(cookie);
				} else if((cookie.getName()).compareTo("sTk") == 0){
					cookie.setValue("");
					cookie.setMaxAge(0);
					sm.getResponse().addCookie(cookie);
				}
			}
		}
		HttpSession session = sm.getRequest().getSession();
		@SuppressWarnings("unchecked")
		Map<String, WebsocketSession> sessionWebsockets = (Map<String, WebsocketSession>) session.getAttribute("sessionWebsockets");
		sm.addWebsockets(sessionWebsockets);
		sm.addToSocket(WebsocketMessage.logoutMessage());
		user.removeWebsockets(sessionWebsockets);
		user.deconnect(sm);
		//session.invalidate();
	}

}
