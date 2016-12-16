package com.Ease.Context;


import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.Ease.Context.Catalog.Catalog;
import com.Ease.Context.Group.GroupManager;
import com.Ease.Context.Group.Infrastructure;
import com.Ease.Dashboard.User.User;
import com.Ease.Utils.DataBase;
import com.Ease.Utils.DataBaseConnection;
import com.Ease.Utils.IdGenerator;
import com.Ease.websocket.WebsocketMessage;
import com.Ease.websocket.WebsocketSession;

public class OnStart implements ServletContextListener{
	@Override
	public void contextDestroyed(ServletContextEvent evt) {
		System.out.println("ServletContextListener destroyed");
	}

	// Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		ServletContext context = evt.getServletContext();
		DataBaseConnection db;
		try {
			db = new DataBaseConnection(DataBase.getConnection());
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
		try {
			evt.getServletContext().setAttribute("serverKey", new ServerKey(db));
			System.out.print("ServletContextListener started...");
			context.setAttribute("idGenerator", new IdGenerator());
			context.setAttribute("catalog", new Catalog(db, context));
			context.setAttribute("groupManager", new GroupManager());
			List<String> colors = new ArrayList<String>();
			colors.add("#ffe74c");
			colors.add("#35a7ff");
			colors.add("#6bf178");
			colors.add("#ec555b");
			colors.add("#805b9b");
			colors.add("#ff974f");
			colors.add("#373b60");
			colors.add("#ff618a");
			context.setAttribute("colors", colors);
			Infrastructure.loadInfrastructures(db, evt.getServletContext());
			Map<String, User> usersMap = new HashMap<String, User>();
			context.setAttribute("users", usersMap);
			System.out.println("done.");
		} catch (Exception e1) {
			e1.printStackTrace();
			return ;
		}
	}
}