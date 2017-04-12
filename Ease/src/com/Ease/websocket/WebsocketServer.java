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
	public void open(Session session, EndpointConfig config) throws GeneralException{
		this.config = config;
		HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
		User user = null;
		user = (User) httpSession.getAttribute("user");
		WebsocketSession wSession = new WebsocketSession(session);
		Map<String, WebsocketSession> sessionWebsockets = null;
		sessionWebsockets = (Map<String, WebsocketSession>) httpSession.getAttribute("sessionWebsockets");
		if (sessionWebsockets == null) {
			sessionWebsockets = new HashMap<String, WebsocketSession>();
			httpSession.setAttribute("sessionWebsockets", sessionWebsockets);
		}
		//throw new NullPointerException("Open websocket " + (user == null ? "no user" : user.getFirstName()) + " socketId : " + wSession.getSessionId());
		System.out.println("Open websocket " + (user == null ? "no user" : user.getFirstName()) + " socketId : " + wSession.getSessionId());
		sessionWebsockets.put(wSession.getSessionId(), wSession);
//		if (user != null)
//			user.addWebsocket(wSession);
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
			System.out.println("Close socket " + (user == null ? "no user" : user.getFirstName()) + " socketId : " + session.getId());
			@SuppressWarnings("unchecked")
			Map<String, WebsocketSession> sessionWebsockets = (Map<String, WebsocketSession>) httpSession.getAttribute("sessionWebsockets");
			sessionWebsockets.remove(session.getId());
//			if (user != null)
//				user.removeWebsocket(session);
		} catch (IllegalStateException ise) {
			//httpSession invalid
		}
	}

	@OnError
	public void onError(Throwable error) {
		String msg = error.toString()+".\nStackTrace :";
		for(int i = 0; i < error.getStackTrace().length; i++){
			msg += "\n" + error.getStackTrace()[i];
		}
		throw new NullPointerException(msg);
	}

	@OnMessage
	public void handleMessage(String message, Session session) throws GeneralException {
		throw new GeneralException(ServletManager.Code.ClientError, "Bullshit");
	}
}
