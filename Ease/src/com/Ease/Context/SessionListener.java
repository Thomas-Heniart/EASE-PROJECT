package com.Ease.Context;


import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.Ease.Dashboard.User.User;

public class SessionListener implements HttpSessionListener {

	
	public void sessionCreated(HttpSessionEvent se) {
		
	}
	
	public void sessionDestroyed(HttpSessionEvent se) {
		
		HttpSession session = se.getSession();
		Map<String, User> sessionIdUserMap = (Map<String, User>) session.getServletContext().getAttribute("sessionIdUserMap");
		sessionIdUserMap.remove(session.getId());
		System.out.println("SEEEESSSSIONNN DESSTROYYYY");
	}
}
