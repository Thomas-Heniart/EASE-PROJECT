package com.Ease.websocket;

import java.io.IOException;
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
@ServerEndpoint(value="/actions", configurator=GetHttpSessionConfigurator.class)
public class WebsocketServer {

	private EndpointConfig config;
	
	@OnOpen
	public void open(Session session, EndpointConfig config) throws GeneralException {
		this.config = config;
		HttpSession httpSession = (HttpSession)config.getUserProperties().get("httpSession");
		User user = (User)httpSession.getAttribute("user");
		if (user == null) {
			try {
				session.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		user.addWebsocket(session);
	}

	@OnClose
	public void close(Session session) {
		HttpSession httpSession = (HttpSession)config.getUserProperties().get("httpSession");
		User user = (User)httpSession.getAttribute("user");
		if (user == null)
			return;
		user.removeWebsocket(session);
		//session.getBasicRemote().sendText(arg0);
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
