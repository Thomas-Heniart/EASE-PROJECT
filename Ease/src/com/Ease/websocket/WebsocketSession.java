package com.Ease.websocket;

import java.io.IOException;

import javax.websocket.Session;

public class WebsocketSession {

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
