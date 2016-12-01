package com.Ease.websocket;

import java.io.IOException;

import javax.websocket.Session;

import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

public class WebsocketSession {
	
	protected Session session;
	
	public WebsocketSession(Session session) {
		this.session = session;
	}
	
	public void sendMessage(WebsocketMessage message) throws GeneralException {
		try {
			this.session.getBasicRemote().sendText(message.getJSONString());
		} catch (IOException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
