package com.Ease.context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBase {
	//Class.forName("com.mysql.jdbc.Driver");
	String url = "jdbc:mysql://localhost:3306/test";
	String login = "client";
	String password = "P6au23q7";
	Connection con = null;
	
	public int connect(){
		try {
			if(con == null || con.isClosed()){
				con = DriverManager.getConnection(url, login, password);
			}
			/*if (con != null) {
				con.close();
				con = null;
			}*/				
		} catch (SQLException e) {
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
	
	public Integer set(String request) throws SQLException {
		Statement stmt = con.createStatement();
		stmt.executeUpdate(request, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();
		Integer id_row = -1;
        if (rs.next()){
            id_row = rs.getInt(1);
        }
        rs.close();
        return id_row;
	}
	
	public boolean start(){
		try {
			con.createStatement().executeUpdate("START TRANSACTION");
			return true;
		} catch (SQLException e) {
			System.out.println("Impossible to set DB");
			e.printStackTrace();
			return false;
		}
	}
	
	public void commit(boolean transaction){
		if(transaction){
			try {
				con.createStatement().executeUpdate("COMMIT");
			} catch (SQLException e) {
				System.out.println("Impossible to set DB");
				e.printStackTrace();
			}
		}
	}
	
	public void cancel(boolean transaction){
		if(transaction){
			try {
				con.createStatement().executeUpdate("ROLLBACK");
			} catch (SQLException e) {
				System.out.println("Impossible to set DB");
				e.printStackTrace();
			}
		}
	}
}
