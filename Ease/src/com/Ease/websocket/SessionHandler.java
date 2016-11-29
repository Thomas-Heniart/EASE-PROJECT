package com.Ease.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

import com.Ease.dashboard.User;
import com.Ease.utils.GeneralException;
import com.Ease.utils.ServletManager;

@ApplicationScoped
public class SessionHandler {
	public static SessionHandler uniqueInstance = new SessionHandler();
	private final Set<User> users = new HashSet<User>();

	public static SessionHandler getInstance() {
		if (uniqueInstance == null)
			uniqueInstance = new SessionHandler();
		return uniqueInstance;
	}

	public void addUser(User user) {
		this.users.add(user);
	}
	
	public void removeUser(User user) {
		this.users.remove(user);
	}
	
	public void removeSession(Session session, Map<String, User> users) {
		for (User user : this.users) {
			if (user.removeSession(session)) {
				user.removeFromContextIfNeeded(users);
				return;
			}
		}
	}
	
	private void sendToSession(Session session, WebsocketMessage message) throws GeneralException {
		try {
			session.getBasicRemote().sendText(message.getJSONString());
		} catch (IOException e) {
			e.printStackTrace();
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}

	private void sendToUser(User user, WebsocketMessage message) throws GeneralException {
		for (Session session : user.getSessions())
			sendToSession(session, message);
	}
	
	private void sendToUsers(List<User> users, WebsocketMessage message) throws GeneralException {
		for (User user : users) {
			sendToUser(user, message);
		}
	}
}