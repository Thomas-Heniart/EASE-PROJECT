package com.Ease.context;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Color {
	enum ColorData {
		NOTHING,
		ID,
		COLOR
	}
	int id;
	String color;
	
	public Color(ResultSet rs) {
		try {
			id = Integer.parseInt(rs.getString(ColorData.ID.ordinal()));
			color = rs.getString(ColorData.COLOR.ordinal());
		} catch (SQLException e) {
			return ;
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getColor() {
		return color;
	}
}
