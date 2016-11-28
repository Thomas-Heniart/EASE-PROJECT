package com.Ease.dashboard;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.Ease.utils.DataBase;
import com.Ease.utils.DataBaseConnection;

public class OnStart implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		System.out.print("ServletContextListener started...");
		ServletContext context = evt.getServletContext();
		DataBaseConnection db;
		try {
			db = new DataBaseConnection(DataBase.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			List<Group> groups = Group.loadGroups(db);
			Map<String, Group> groupsMap = new HashMap<String, Group>();
			groupsMap.putAll(Group.getGroupMap(groups));
			evt.getServletContext().setAttribute("infra", groups);
			evt.getServletContext().setAttribute("groups", groupsMap);
			
			Map<String, GroupProfile> groupProfilesMap = new HashMap<String, GroupProfile>();
			groupProfilesMap.putAll(Group.getGroupProfileMap(groups));
			evt.getServletContext().setAttribute("groupProfiles", groups);
			// SiteManager initialization
			/*SiteManager siteManager = new SiteManager();
			siteManager.refresh(db);
			context.setAttribute("siteManager", siteManager);
			ResultSet rs = db.get("SELECT * FROM tags;");
			while (rs.next()) {
				siteManager.addNewTag(new Tag(rs, context));
			}
			siteManager.setTagsForSites(context);
			siteManager.setSitesForTags(context);
			*/
			Map<String, User> usersMap = new HashMap<String, User>();
			context.setAttribute("users", usersMap);
			System.out.println("done.");
		} catch (Exception e1) {
			e1.printStackTrace();
			return ;
		}
	}
}