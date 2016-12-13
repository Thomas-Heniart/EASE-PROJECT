package com.Ease.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
@ServerEndpoint(value = "/actions", configurator = GetHttpSessionConfigurator.class)
public class WebsocketServer {

	private EndpointConfig config;

	@SuppressWarnings("unchecked")
	@OnOpen
	public void open(Session session, EndpointConfig config) throws GeneralException {
		this.config = config;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) httpSession.getAttribute("user");
		WebsocketSession wSession = new WebsocketSession(session);
		System.out.println("Open websocket " + (user == null ? "no user" : user.getFirstName()));
		if (user == null) {
			Map<String, WebsocketSession> unconnectedSessions = (Map<String, WebsocketSession>) httpSession
					.getAttribute("unconnectedSessions");
			if (unconnectedSessions == null) {
				unconnectedSessions = new HashMap<String, WebsocketSession>();
				httpSession.setAttribute("unconnectedSessions", unconnectedSessions);
			}
			unconnectedSessions.put(wSession.getSessionId(), wSession);
		} else {
			Map<String, WebsocketSession> browserWebsockets = (Map<String, WebsocketSession>) httpSession
					.getAttribute("browserWebsockets");
			if (browserWebsockets == null) {
				browserWebsockets = new HashMap<String, WebsocketSession>();
				httpSession.setAttribute("browserWebsockets", browserWebsockets);
			}
			browserWebsockets.put(wSession.getSessionId(), wSession);
			user.addWebsocket(wSession);
			System.out.println("socketId : " + wSession.getSessionId());
			System.out.println(user.getFirstName() + " websockets size : " + user.getWebsockets().size());
		}
		try {
			wSession.sendMessage(WebsocketMessage.assignIdMessage(wSession.getSessionId()));
		} catch (IOException e) {
			throw new GeneralException(ServletManager.Code.ClientError, e);
		}
	}

	@OnClose
	public void close(Session session) throws GeneralException {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		try {
			httpSession.getCreationTime();
			User user = (User) httpSession.getAttribute("user");
			System.out.println("Close session " + (user == null ? "no user" : user.getFirstName()));
			if (user == null)
				WebsocketSession.removeWebsocketSession(session, httpSession);
			else {
				@SuppressWarnings("unchecked")
				Map<String, WebsocketSession> browserWebsockets = (Map<String, WebsocketSession>) httpSession.getAttribute("browserWebsockets");
				browserWebsockets.remove(session.getId());
				System.out.println(user == null);
				user.removeWebsocket(session);
			}
		} catch (IllegalStateException ise) {
			// Invalid session

		}
	}

	@OnError
	public void onError(Throwable error) {
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws GeneralException {
		throw new GeneralException(ServletManager.Code.ClientError, "Bullshit");
	}
}
