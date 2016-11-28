package com.Ease.dashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OnStart implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		//test it
		
		List<Group> groups = Group.loadGroups(db);
		Map<String, Group> groupsMap = new HashMap<String, Group>();
		groupsMap.putAll(Group.getGroupMap(groups));
		evt.getServletContext().setAttribute("groups", groupsMap);
		
		Map<String, GroupProfile> groupProfilesMap = new HashMap<String, GroupProfile>();
		groupProfilesMap.putAll(Group.getGroupProfileMap(groups));
		evt.getServletContext().setAttribute("groupProfiles", groups);
	}
}