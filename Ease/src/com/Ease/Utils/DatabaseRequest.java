package com.Ease.Utils;

import org.apache.commons.lang3.StringEscapeUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


public class DatabaseRequest {
    protected PreparedStatement statement;
    protected int parameterCount;
    protected String request;

    public DatabaseRequest(Connection con, String request) throws HttpServletException {
        try {
            this.request = request;
            this.statement = con.prepareStatement(request, PreparedStatement.RETURN_GENERATED_KEYS);
            this.parameterCount = 0;
        } catch (SQLException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setObject(Object value, int type) throws HttpServletException {
        this.parameterCount++;
        try {
            this.statement.setObject(this.parameterCount, value, type);
        } catch (SQLException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setObject(Object value) throws HttpServletException {
        this.parameterCount++;
        try {
            this.statement.setObject(this.parameterCount, value);
        } catch (SQLException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setInt(int value) throws HttpServletException {
        this.setObject(value);
    }

    public void setInt(String value) throws HttpServletException {
        try {
            this.setInt(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public void setInt(int value, int type) throws HttpServletException {
        this.setObject(value, type);
    }

    public void setBoolean(boolean bool) throws HttpServletException {
        this.setObject(bool);
    }

    public void setString(String value) throws HttpServletException {
        this.setObject(StringEscapeUtils.escapeHtml4(value));
    }

    public void setNull() throws HttpServletException {
        this.setObject(null);
    }

    public void setDate(Date date) throws HttpServletException {
        this.setObject(date);
    }

    public DatabaseResult get() throws HttpServletException {
        try {
            ResultSet rs = this.statement.executeQuery();
            //System.out.println(this.statement.toString());
            return new DatabaseResult(rs);
        } catch (SQLException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }
    }

    public Integer set() throws HttpServletException {
        try {
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            //System.out.println(statement.toString());
            Integer id_row = null;
            if (rs.next()) {
                try {
                    id_row = Integer.parseInt(rs.getString(1));
                } catch (NumberFormatException e) {
                    throw new HttpServletException(HttpStatus.InternError, e);
                }
            }
            rs.close();
            statement.close();
            return id_row;
        } catch (SQLException e) {
            throw new HttpServletException(HttpStatus.InternError, e);
        }

    }
}
