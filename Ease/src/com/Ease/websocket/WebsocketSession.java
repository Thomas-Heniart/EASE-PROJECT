package com.Ease.websocket;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

public class WebsocketSession {

	public static void removeWebsocketSession(Session session, HttpSession httpSession) {
		@SuppressWarnings("unchecked")
		Map<String, WebsocketSession> unconnectedSessions = (Map<String, WebsocketSession>) httpSession.getAttribute("unconnectedSessions");
		if (unconnectedSessions == null)
			return;
		for (Map.Entry<String, WebsocketSession> entry : unconnectedSessions.entrySet()) {
			if (entry.getValue() == session) {
				unconnectedSessions.remove(entry.getKey());
				return;
			}
		}
	}
	
	public static void removeAll(HttpSession httpSession) {
		@SuppressWarnings("unchecked")
		Map<String, WebsocketSession> unconnectedSessions = (Map<String, WebsocketSession>) httpSession.getAttribute("unconnectedSessions");
		unconnectedSessions.clear();
	}
	
	protected Session session;
	
	public WebsocketSession(Session session) {
		this.session = session;
	}
	
	public void sendMessage(WebsocketMessage message) throws IOException {
		this.session.getBasicRemote().sendText(message.getJSONString());
	}
	
	public String getSessionId() {
		return this.session.getId();
	}
	
	public Session getSession() {
		return this.session;
	}
}
