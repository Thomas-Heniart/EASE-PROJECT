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

	@OnOpen
	public void open(Session session, EndpointConfig config) throws GeneralException {
		this.config = config;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) httpSession.getAttribute("user");
		WebsocketSession wSession = new WebsocketSession(session);
		if (user == null) {
			@SuppressWarnings("unchecked")
			Map<String, WebsocketSession> unconnectedSessions = (Map<String, WebsocketSession>) httpSession.getAttribute("unconnectedSessions");
			if (unconnectedSessions == null) {
				unconnectedSessions = new HashMap<String, WebsocketSession>();
				httpSession.setAttribute("unconnectedSessions", unconnectedSessions);
			}
			unconnectedSessions.put(wSession.getSessionId(), wSession);
		} else {
			user.addWebsocket(wSession);
		}
		try {
			System.out.println(wSession.getSessionId());
			wSession.sendMessage(WebsocketMessage.assignIdMessage(wSession.getSessionId()));
		} catch (IOException e) {
			throw new GeneralException(ServletManager.Code.ClientError, e);
		}
	}

	@OnClose
	public void close(Session session) {
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = (User) httpSession.getAttribute("user");
		if (user == null)
			WebsocketSession.removeWebsocketSession(session, httpSession);
		else
			user.removeWebsocket(session);
	}

	@OnError
	public void onError(Throwable error) {
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws GeneralException {
		throw new GeneralException(ServletManager.Code.ClientError, "Bullshit");
	}
}
