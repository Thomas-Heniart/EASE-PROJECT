package com.Ease.Utils;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public final class LogsDatabase {
	private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/easeLogs?charset=utf8");
        dataSource.setUsername("client");
        dataSource.setPassword("P6au23q7");
    }

    private LogsDatabase() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
