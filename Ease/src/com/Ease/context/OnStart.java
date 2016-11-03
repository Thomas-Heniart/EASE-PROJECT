package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.Ease.context.Color.ColorData;
import com.Ease.context.Tag.TagData;

public class OnStart implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		System.out.print("ServletContextListener started...");
		ServletContext context = evt.getServletContext();

		DataBase db = new DataBase();
		context.setAttribute("DataBase", db);
		if (db.connect() != 0) {
			return;
		}

		// Récupération des sites et des options de connection
		
		SiteManager siteManager = new SiteManager();
		try {
			ResultSet rs = db.get("SELECT * FROM websites ORDER BY website_name;");
			while (rs.next()) {
				siteManager.add(new Site(rs));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load websites.");
			return;
		}
		context.setAttribute("siteManager", siteManager);
		
		try {
			ResultSet rs = db.get("SELECT * FROM tags;");
			while (rs.next()) {
				siteManager.addNewTag(new Tag(rs, context));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// Set tags for websites
		try {
			siteManager.setTagsForSites(context);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// Set websites for tags
		try {
			siteManager.setSitesForTags(context);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		

		// Récupération des couleurs de profil

		List<Color> colors = new LinkedList<Color>();
		try {
			ResultSet rs = db.get("SELECT * FROM colors;");
			while (rs.next()) {
				colors.add(new Color(rs));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load colors.");
			return;
		}
		context.setAttribute("Colors", colors);
		
		//Intialize adminMessage
		AdminMessage adminMessage = new AdminMessage();
		context.setAttribute("AdminMessage", adminMessage);
		
		System.out.println("done.");
		db.close();
	}
}