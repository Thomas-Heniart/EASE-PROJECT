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

		// Récupération des tags
		List<Tag> tags = new LinkedList<Tag>();
		try {
			ResultSet rs = db.get("SELECT * FROM tags;");
			while (rs.next()) {
				tags.add(new Tag(rs, context));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load tags");
			return;
		}
		context.setAttribute("Tags", tags);

		List<TagAndSiteMap> tagAndSiteMapping = new LinkedList<TagAndSiteMap>();
		try {
			ResultSet rs = db.get("SELECT * FROM TagAndSiteMap;");
			while (rs.next()) {
				tagAndSiteMapping.add(new TagAndSiteMap(rs, context));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load tag and site map" + e.getMessage());
		}
		context.setAttribute("TagAndSiteMappging", tagAndSiteMapping);

		// Récupération des sites et des options de connection

		SiteManager sites = new SiteManager();
		try {
			ResultSet rs = db.get("SELECT * FROM websites;");
			while (rs.next()) {
				sites.add(new Site(rs));
			}
		} catch (SQLException e) {
			System.out.println("Fail to load websites.");
			return;
		}
		context.setAttribute("Sites", sites);
		try {
			ResultSet rs = db.get("SELECT * FROM tags;");
			while (rs.next()) {
				sites.addNewTag(new Tag(rs, context));
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		// Set tags for websites
		try {
			sites.setTagsForSites(context);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		// Set websites for tags
		try {
			sites.setSitesForTags(context);
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

		System.out.println("done.");
		db.close();
	}
}