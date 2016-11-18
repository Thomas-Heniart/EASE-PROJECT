package com.Ease.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseConnection {

	protected Connection	con;
	protected int			transaction;
	
	public DataBaseConnection(Connection con) {
		this.con = con;
		this.transaction = 0;
	}
	
	public ResultSet get(String request) throws GeneralException {
		
		ResultSet rs;
		try {
			rs = con.createStatement().executeQuery(request);
			return rs;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Integer set(String request) throws GeneralException {
		
		try {
			Statement stmt = con.createStatement();
			stmt.executeUpdate(request, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.getGeneratedKeys();
			Integer id_row = -1;
	        if (rs.next()){
	            id_row = rs.getInt(1);
	        }
	        rs.close();
	        return id_row;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public int startTransaction() throws GeneralException {
		this.transaction++;
		if (this.transaction == 1) {
			try {
				con.createStatement().executeUpdate("START TRANSACTION");
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		}
		return (this.transaction);
	}
	
	public void commitTransaction(int transaction) throws GeneralException {
		if (this.transaction == transaction) {
			this.transaction--;
			if (this.transaction == 0) {
				try {
					con.createStatement().executeUpdate("COMMIT");
				} catch (SQLException e) {
					throw new GeneralException(ServletManager.Code.InternError, e);
				}
			}
		} else {
			throw new GeneralException(ServletManager.Code.InternError, "Bad transaction commit");
		}
	}
	public void rollbackTransaction() throws GeneralException {
		if (this.transaction > 0) {
			try {
				con.createStatement().executeUpdate("ROLLBACK");
			} catch (SQLException e) {
				throw new GeneralException(ServletManager.Code.InternError, e);
			}
		}
	}
}
