package com.Ease.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import org.json.simple.JSONObject;

import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

@ApplicationScoped
public class SessionHandler {
	public static SessionHandler uniqueInstance = new SessionHandler();
	private int tabId = 0;
	private final Set<Session> sessions = new HashSet<>();

	public static SessionHandler getInstance() {
		if (uniqueInstance == null)
			uniqueInstance = new SessionHandler();
		return uniqueInstance;
	}

	public void addSession(Session session) throws GeneralException {
		sessions.add(session);
		sendToSession(session, tabId);
	}

	public void removeSession(Session session) {
		sessions.remove(session);
	}

	

	private void sendToAllConnectedSessions(WebsocketMessage message) throws GeneralException {
		for (Session session : sessions) {
			sendToSession(session, message);
		}
	}

	private void sendToSession(Session session, WebsocketMessage message) throws GeneralException {
		try {
			session.getBasicRemote().sendText(message.getJSONString());
		} catch (IOException e) {
			e.printStackTrace();
			sessions.remove(session);
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	private void sendToSession(Session session, int tabId) throws GeneralException {
		try {
			session.getBasicRemote().sendText(String.valueOf(tabId++));
		} catch (IOException e) {
			e.printStackTrace();
			sessions.remove(session);
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

}
