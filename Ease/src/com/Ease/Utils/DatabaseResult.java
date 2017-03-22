package com.Ease.Utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.StringEscapeUtils;

public class DatabaseResult {
	protected ResultSet rs;
	
	public DatabaseResult(ResultSet rs) {
		this.rs = rs;
	}
	
	public boolean next() throws GeneralException {
		try {
			return this.rs.next();
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Object getObject(int columnIndex) throws GeneralException {
		try {
			Object res = this.rs.getObject(columnIndex);
			if (res.getClass().getName().equals(String.class.getName()))
				res = StringEscapeUtils.unescapeHtml4((String)res);
			return res;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Object getObject(String columnName) throws GeneralException {
		try {
			Object res = this.rs.getObject(columnName);
			if (res.getClass().getName().equals(String.class.getName()))
				res = StringEscapeUtils.unescapeHtml4((String)res);
			return res;
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public String getString(int columnIndex) throws GeneralException {
		try {
			return StringEscapeUtils.unescapeHtml4(this.rs.getString(columnIndex));
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public String getString(String columnName) throws GeneralException {
		try {
			return this.rs.getString(columnName);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Integer getInt(int columnIndex) throws GeneralException {
		try {
			return this.rs.getInt(columnIndex);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Integer getInt(String columnName) throws GeneralException {
		try {
			return this.rs.getInt(columnName);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public Boolean getBoolean(int columnIndex) throws GeneralException {
		try {
			return this.rs.getBoolean(columnIndex);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
	
	public boolean getBoolean(String columnName) throws GeneralException {
		try {
			return this.rs.getBoolean(columnName);
		} catch (SQLException e) {
			throw new GeneralException(ServletManager.Code.InternError, e);
		}
	}
}
