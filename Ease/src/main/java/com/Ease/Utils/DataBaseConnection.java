package com.Ease.Utils;

import java.sql.*;

public class DataBaseConnection {

	protected Connection	con;
	protected int			transaction;
	protected PreparedStatement statement;
	protected DatabaseRequest request;
	
	public DataBaseConnection(Connection con) {
		this.con = con;
		this.transaction = 0;
	}
	
	public DatabaseRequest prepareRequest(String request) throws HttpServletException {
		this.request = new DatabaseRequest(con, request);
		return this.request;
	}
	
	public DatabaseResult get() throws HttpServletException {
		return this.request.get();
	}
	
	public Integer set() throws HttpServletException {
		return this.request.set();
	}
	
	public ResultSet get(String request) throws HttpServletException {
		
		ResultSet rs;
		try {
			
			//rs = statement.executeQuery();
			rs = con.createStatement().executeQuery(request);
			return rs;
		} catch (SQLException e) {
			throw new HttpServletException(HttpStatus.InternError, e);
		}
	}
	
	public Integer set(String request) throws HttpServletException {
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(request, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			Integer id_row = null;
	        if (rs.next()){
	        	try {
	        		id_row = Integer.parseInt(rs.getString(1));
	        	} catch (NumberFormatException e) {
	        		id_row = null;
	        	}
	        }
	        rs.close();
	        stmt.close();
	        return id_row;
		} catch (SQLException e) {
			throw new HttpServletException(HttpStatus.InternError, e);
		}
	}
	
	public int startTransaction() throws HttpServletException {
		this.transaction++;
		if (this.transaction == 1) {
			try {
				con.createStatement().executeUpdate("START TRANSACTION");
			} catch (SQLException e) {
				throw new HttpServletException(HttpStatus.InternError, e);
			}
		}
		return (this.transaction);
	}
	
	public void commitTransaction(int transaction) throws HttpServletException {
		if (this.transaction == transaction) {
			this.transaction--;
			if (this.transaction == 0) {
				try {
					con.createStatement().executeUpdate("COMMIT");
				} catch (SQLException e) {
					throw new HttpServletException(HttpStatus.InternError, e);
				}
			}
		} else {
			throw new HttpServletException(HttpStatus.InternError, "Bad transaction commit");
		}
	}
	public void rollbackTransaction() throws HttpServletException {
		if (this.transaction > 0) {
			try {
				con.createStatement().executeUpdate("ROLLBACK");
			} catch (SQLException e) {
				throw new HttpServletException(HttpStatus.InternError, e);
			}
		}
	}
	
	public void close() {
		try {
			con.close();
		} catch (SQLException e) {
			System.err.println("DB Con close failed.");
		}
	}
}
