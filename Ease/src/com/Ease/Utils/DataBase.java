package com.Ease.Utils;

import com.Ease.Context.Variables;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;


public final class DataBase {
	private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDefaultAutoCommit(true);
        dataSource.setInitialSize(10);
        dataSource.setMaxIdle(100);
        dataSource.setMinIdle(10);
        dataSource.setMaxWaitMillis(30000);
        dataSource.setTimeBetweenEvictionRunsMillis(5000);
        dataSource.setMinEvictableIdleTimeMillis(60000);
        dataSource.setPoolPreparedStatements(true);
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + Variables.DATABASE_IP + ":3306/ease?charset=utf8");
        dataSource.setUsername("client");
        dataSource.setPassword("P6au23q7");
    }

    private DataBase() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
