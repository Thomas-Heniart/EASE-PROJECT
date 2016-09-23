package com.Ease.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletContext;

import com.Ease.context.DataBase;

public class Group {
	protected String		id;
	protected String 		name;
	protected List<Group>	sons;
	
	Group(ResultSet rs, ServletContext context){
		sons = new LinkedList<Group>();
		DataBase db = ((DataBase) (context.getAttribute("DataBase")));
		try {
			id = rs.getString(1);
			name = rs.getString(2);
			ResultSet rs2 = db.get("SELECT * FROM groups WHERE parent = "+ id + ";");
			while (rs2.next()) {
				sons.add(new Group(rs2, context));
			}
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	public static Group getGroup(String group_id, ServletContext context){
		DataBase db = ((DataBase) (context.getAttribute("DataBase")));
		ResultSet rs = null;
		String parent = "null";
		
		rs = db.get("SELECT * FROM groups WHERE id = "+ group_id + ";");
		try {
			rs.next();
			parent = rs.getString(3);
			if (parent == null || parent.equals("null")) {
				return new Group(rs, context);
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
}
