package com.Ease.websocket;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.Ease.utils.GeneralException;

@ApplicationScoped
@ServerEndpoint("/actions")
public class WebsocketServer {

	@Inject
	private final SessionHandler sessionHandler = SessionHandler.getInstance();

	@OnOpen
	public void open(Session session) throws GeneralException {
		sessionHandler.addSession(session);
	}

	@OnClose
	public void close(Session session) {
		sessionHandler.removeSession(session);
	}

	@OnError
	public void onError(Throwable error) {
		Logger.getLogger(WebsocketServer.class.getName()).log(Level.SEVERE, null, error);
	}

	@OnMessage
	public void handleMessage(String message, Session session) {
		sessionHandler.removeSession(session);
	}
}
