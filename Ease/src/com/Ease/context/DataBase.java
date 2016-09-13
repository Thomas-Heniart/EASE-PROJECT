package com.Ease.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBase {
	//Class.forName("com.mysql.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3306/test";
	String login = "client";
	String password = "P6au23q7";
	Connection con;
	
	public int connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection(url, login, password);
		} catch (SQLException e) {
			System.out.println("Impossible to connect DB");
			return 1;
		} catch (ClassNotFoundException e) {
			System.out.println("Impossible to connect DB");
			return 1;
		}
		return 0;
	}
	
	public int close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.out.println("Impossible to close DB");
			return 1;
		}
		return 0;
	}
	
	public ResultSet get(String request) {
		try {
			ResultSet rs = con.createStatement().executeQuery(request);
			return rs;
		} catch (SQLException e) {
			System.out.println("Impossible to get DB");
			e.printStackTrace();
			return null;
		}
	}
	
	public int set(String request) {
		try {
			con.createStatement().executeUpdate(request);
			System.out.println(request);
			return 0;
		} catch (SQLException e) {
			System.out.println("Impossible to set DB");
			e.printStackTrace();
			return 1;
		}
	}
}
