package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OnStart implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		System.out.print("ServletContextListener started...");
		List<Color> colors = new LinkedList<Color>();
		ServletContext context = evt.getServletContext();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
			return;
		}
		DataBase db = new DataBase();
		context.setAttribute("DataBase", db);
		if (db.connect() != 0) {
			return;
		}

		
		try {
			// SiteManager initialization
			SiteManager siteManager = new SiteManager();
			siteManager.refresh(db);
			context.setAttribute("siteManager", siteManager);
			ResultSet rs = db.get("SELECT * FROM tags;");
			while (rs.next()) {
				siteManager.addNewTag(new Tag(rs, context));
			}
			siteManager.setTagsForSites(context);
			siteManager.setSitesForTags(context);
			
			// Colors initializations
			rs = db.get("SELECT * FROM colors;");
			while (rs.next()) {
				colors.add(new Color(rs));
			}
			context.setAttribute("Colors", colors);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		//Intialize adminMessage
		AdminMessage adminMessage = new AdminMessage();
		context.setAttribute("AdminMessage", adminMessage);
		
		System.out.println("done.");
		db.close();
	}
}