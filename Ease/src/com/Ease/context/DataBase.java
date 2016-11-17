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
	
	public void connect() throws SQLException {
		if(con == null || con.isClosed()){
			con = DriverManager.getConnection(url, login, password);
		}				
	}
	
	public void close() throws SQLException {
		con.close();
	}
	
	public ResultSet get(String request) throws SQLException {
		ResultSet rs = con.createStatement().executeQuery(request);
		return rs;
		
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
	
	public boolean start() throws SQLException {
		con.createStatement().executeUpdate("START TRANSACTION");
		return true;
	}
	
	public void commit(boolean transaction) throws SQLException{
		if (transaction)
			con.createStatement().executeUpdate("COMMIT");
	}
	
	public void cancel(boolean transaction) throws SQLException {
		if (transaction)
			con.createStatement().executeUpdate("ROLLBACK");
	}
}
