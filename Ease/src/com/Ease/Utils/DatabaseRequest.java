package com.Ease.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringEscapeUtils;


public class DatabaseRequest {
	protected PreparedStatement statement;
	protected int parameterCount;
	protected String request;
	
	public DatabaseRequest(Connection con, String request) throws GeneralException {
		try {
			this.request = request;
			this.statement = con.prepareStatement(StringEscapeUtils.escapeHtml4(request), PreparedStatement.RETURN_GENERATED_KEYS);
			this.parameterCount = 0;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void setObject(Object value, int type) throws GeneralException {
		this.parameterCount++;
		try {
			this.statement.setObject(this.parameterCount, value, type);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void setObject(Object value) throws GeneralException {
		this.parameterCount++;
		try {
			this.statement.setObject(this.parameterCount, value);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public void setInt(int value) throws GeneralException {
		this.setObject(value);
	}
	
	public void setInt(String value) throws GeneralException {
		try {
			this.setInt(Integer.valueOf(value));
    	} catch (NumberFormatException e) {
    		throw new GeneralException(ServletManager.Code.ClientError, e);
    	}
	}
	
	public void setInt(int value, int type) throws GeneralException {
		this.setObject(value, type);
	}
	
	public void setBoolean(boolean bool) throws GeneralException {
		this.setObject(bool);
	}
	
	public void setString(String value) throws GeneralException {
		this.setObject(StringEscapeUtils.escapeHtml4(value));
	}
	
	public void setNull() throws GeneralException {
		this.setObject(null);
	}
	
	public DatabaseResult get() throws GeneralException {
		try {
			return new DatabaseResult(this.statement.executeQuery());
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Integer set() throws GeneralException {
		try {
			statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			Integer id_row = null;
			if (rs.next()){
	        	try {
	        		id_row = Integer.parseInt(rs.getString(1));
	        	} catch (NumberFormatException e) {
	        	}
	        }
			rs.close();
			statement.close();
			return id_row;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
		
	}
}
