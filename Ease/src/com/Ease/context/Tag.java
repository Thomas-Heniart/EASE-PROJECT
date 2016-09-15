package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletContext;

import com.Ease.context.Color.ColorData;

public class Tag {
	enum TagData {
		NOTHING, ID, NAME, COLOR_ID
	}

	protected String name;
	protected String color_id;
	protected String color;
	protected int id;

	public Tag(ResultSet rs, ServletContext context) {
		try {
			id = Integer.parseInt(rs.getString(TagData.ID.ordinal()));
			name = rs.getString(TagData.NAME.ordinal());
			color_id = rs.getString(TagData.COLOR_ID.ordinal());
			setColor(context);
			System.out.println(color);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor_id() {
		return color_id;
	}

	public void setColor_id(String color_id) {
		this.color_id = color_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setColor(ServletContext context) throws SQLException {
		DataBase db = (DataBase) context.getAttribute("DataBase");
		ResultSet rs = db.get("SELECT colors.color FROM colors, tags WHERE id=" + color_id + ";");
		rs.next();
		this.color = rs.getString(1); /* 1 because there is only one column in response */
	}

	public String getColor() {
		return color;
	}
}
