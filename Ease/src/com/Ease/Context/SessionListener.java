package com.Ease.Context;


import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("Connection: " + se.getSession().getId());
	}
	
	public void sessionDestroyed(HttpSessionEvent se) {
		
		HttpSession session = se.getSession();
		System.out.println("Session destroy : " + session.getId());
	}
}
