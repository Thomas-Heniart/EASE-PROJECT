package com.Ease.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;

public class Group {
	protected String		id;
	protected String 		classId;
	protected String 		name;
	protected List<Group>	sons;
	
	Group(ResultSet rs, String classId, ServletContext context){
		sons = new LinkedList<Group>();
		DataBase db = ((DataBase) (context.getAttribute("DataBase")));
		try {
			id = rs.getString(1);
			this.classId = classId;
			name = rs.getString(2);
			ResultSet rs2 = db.get("SELECT * FROM groups WHERE parent = "+ id + ";");
			int i = 0;
			while (rs2.next()) {
				sons.add(new Group(rs2, classId + "x" + i, context));
				i++;
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public static Group getGroup(String group_id, ServletContext context){
		DataBase db = ((DataBase) (context.getAttribute("DataBase")));
		ResultSet rs = null;
		String parent = "null";
		
		try {
			rs = db.get("SELECT * FROM groups WHERE id = "+ group_id + ";");
			rs.next();
			parent = rs.getString(3);
			if (parent == null || parent.equals("null")) {
				return new Group(rs, "0", context);
			} else {
				return getGroup(parent, context);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Group> getSons() {
		return sons;
	}
	public String getName(){
		return name;
	}
	public String getId(){
		return id;
	}
	public String getClassId(){
		return classId;
	}
	
	public List<CustomApp> getCustomApps(ServletContext context) {
		DataBase db = ((DataBase) (context.getAttribute("DataBase")));
		ResultSet rs = null;
		List<CustomApp> customApps = new LinkedList<CustomApp>();
		
		try {
			rs = db.get("SELECT * FROM customApps WHERE group_id = "+ id + ";");
			while (rs.next()) {
				customApps.add(new CustomApp(rs.getString(1), rs.getString(2), Integer.parseInt(rs.getString(3)), rs.getString(4), context));
			}
			return customApps;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean haveThisCustomApp(CustomApp customApp, DataBase db) {
		ResultSet rs = null;
		
		try {
			if (db == null)
				System.out.println("id null");
			if (customApp.getId() == null)
				System.out.println("ca null");
			rs = db.get("SELECT * FROM AppAndGroupMap WHERE group_id="+ id + " AND app_id=" + customApp.getId() + ";");
			if (rs.next()) {
				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}
