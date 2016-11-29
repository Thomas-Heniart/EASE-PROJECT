package com.Ease.websocket;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.Ease.dashboard.User;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

@ApplicationScoped
@ServerEndpoint(value="/actions", configurator=GetHttpSessionConfigurator.class)
public class WebsocketServer {

	private EndpointConfig config;
	
	@Inject
	private final SessionHandler sessionHandler = SessionHandler.getInstance();

	@OnOpen
	public void open(Session session, EndpointConfig config) throws GeneralException {
		this.config = config;
		HttpSession httpSession = (HttpSession)config.getUserProperties().get("httpSession");
		User user = (User)httpSession.getAttribute("user");
		if (user == null)
			return;
		user.addInContext((Map<String, User>)httpSession.getServletContext().getAttribute("users"));
		user.addSession(session);
		sessionHandler.addUser(user);
	}

	@OnClose
	public void close(Session session) {
		HttpSession httpSession = (HttpSession)config.getUserProperties().get("httpSession");
		Map<String, User> users =  (Map<String, User>)httpSession.getServletContext().getAttribute("users");
		sessionHandler.removeSession(session, users);
		
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
