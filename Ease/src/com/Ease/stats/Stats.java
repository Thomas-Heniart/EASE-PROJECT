package com.Ease.stats;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;
import com.Ease.data.ServletItem;
import com.Ease.session.User;

public class Stats {
	public enum Action
	{
		Connect,
		Logout,
		NewUser,
		EditUser,
		DeleteUser,
		AddProfile,
		EditProfile,
		DeleteProfile,
		AddApp,
		EditApp,
		DeleteApp,
		UseApp,
		AskForNewApp,
		UploadWebsite
	}
	public static void saveAction(ServletContext context, User user, Stats.Action action, String msg){
		DataBase db = (DataBase)context.getAttribute("DataBase");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			db.connect();
			if (user != null)
				db.set("INSERT INTO stats VALUES (NULL, " + user.getId() + ", " + action.ordinal() + ", '" + dateFormat.format(date) + "', '" + msg + "');");
			else
				db.set("INSERT INTO stats VALUES (NULL, NULL, " + action.ordinal() + ", '" + dateFormat.format(date) + "', '" + msg + "');");
		} catch (SQLException e) {
			return ;
		}
	}
}