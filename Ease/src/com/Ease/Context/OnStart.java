package com.Ease.Context;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Group.Group;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.App.GroupApp;
import com.Ease.Dashboard.Profile.GroupProfile;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.IdGenerator;

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
			evt.getServletContext().setAttribute("idGenerator", new IdGenerator());
			evt.getServletContext().setAttribute("catalog", new Catalog(db));
			evt.getServletContext().setAttribute("groupManager", new GroupManager());
			Infrastructure.loadInfrastructures(db, evt.getServletContext());
			
						
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