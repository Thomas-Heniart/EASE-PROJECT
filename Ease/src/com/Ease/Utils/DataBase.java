package com.Ease.Utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;


public final class DataBase {
	private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/ease?charset=utf8");
        dataSource.setUsername("client");
        dataSource.setPassword("P6au23q7");
    }

    private DataBase() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
