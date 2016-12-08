package com.Ease.websocket;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.Ease.Dashboard.User.User;
import com.Ease.Utils.GeneralException;
import com.Ease.Utils.ServletManager;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebsocketServer {

	private EndpointConfig config;
	private int tabId = 0;

	@OnOpen
	public void open(Session session) {
		System.out.println("Coucou");
	}
	/*public void open(Session session, EndpointConfig config) throws GeneralException {
		this.config = config;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
		System.out.println("Session find");
		User user = (User) httpSession.getAttribute("user");
		if (user == null) {
			@SuppressWarnings("unchecked")
			Map<String, WebsocketSession> unconnectedSessions = (Map<String, WebsocketSession>) httpSession
					.getAttribute("unconnectedSessions");
			if (unconnectedSessions == null) {
				unconnectedSessions = new HashMap<String, WebsocketSession>();
				httpSession.setAttribute("unconnectedSessions", unconnectedSessions);
			}
			unconnectedSessions.put(String.valueOf(tabId), new WebsocketSession(session));
			System.out.println("Session find");
			tabId++;
		} else {
			user.addWebsocket(session);
		}
	}*/

	@OnClose
	public void close(Session session) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get("httpSession");
		User user = (User) httpSession.getAttribute("user");
		if (user == null)
			return;
		user.removeWebsocket(session);
	}

	@OnError
	public void onError(Throwable error) {
		Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws GeneralException {
		throw new GeneralException(ServletManager.Code.ClientError, "Bullshit");
	}
}
