package com.yjy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 编写JDBC工具类，获取数据库连接
 * 读取配置文件，获取连接，执行一次，
 */
public class DBUtil {
    private static String url;
    private static String username;
    private static String password;

    public static Connection getConnection(String databaseIp, String databaseId, String databaseUserName, String databasePassword) throws SQLException {

        url = "jdbc:oracle:thin:@" + databaseIp + ":1521:" + databaseId;
        username = databaseUserName;
        password = databasePassword;
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("连接数据库失败！");
        }
        return connection;
    }

}