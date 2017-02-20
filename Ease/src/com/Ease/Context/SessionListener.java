package com.Ease.Context;


import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.Ease.Dashboard.User.User;

public class SessionListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("Connection: " + se.getSession().getId());
	}
	
	public void sessionDestroyed(HttpSessionEvent se) {
		
		HttpSession session = se.getSession();
		Map<String, User> users = (Map<String, User>) session.getServletContext().getAttribute("users");
		Map<String, User> sessionIdUserMap = (Map<String, User>) session.getServletContext().getAttribute("sessionIdUserMap");
		Map<String, User> sIdUserMap = (Map<String, User>) session.getServletContext().getAttribute("sIdUserMap");
		
		System.out.println("destroy");
		User user = sessionIdUserMap.get(session.getId());
		if (user != null) {
			System.out.println("destroy: " + session.getId());
			sessionIdUserMap.remove(session.getId());
			sIdUserMap.remove(user.getSessionSave().getSessionId());
			for (Map.Entry<String, User> entry : sessionIdUserMap.entrySet())
			{
				if (entry.getValue() == user) {
					return ;
				}
			}
			users.remove(user.getEmail());
		}
	}
}
