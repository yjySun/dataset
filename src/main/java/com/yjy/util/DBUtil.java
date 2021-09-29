package com.yjy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * 编写JDBC工具类，获取数据库连接
 * 读取配置文件，获取连接，执行一次，
 */
public class DBUtil {
    private static String driverClass;
    private static String url;
    private static String username;
    private static String password;
//    private static DruidDataSource dataSource = new DruidDataSource();


    public static Connection getConnection(String databaseIp, String databaseId, String databaseUserName, String databasePassword) throws SQLException {

        driverClass = "oracle.jdbc.OracleDriver";
        url = "jdbc:oracle:thin:@" + databaseIp + ":1521:" + databaseId;
        username = databaseUserName;
        password = databasePassword;
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new SQLException("连接数据库失败！");
        }
        return connection;
    }

//    public static void initDatabase(String databaseIp, String databaseId, String databaseUserName, String databasePassword) {
//
//        driverClass = "oracle.jdbc.OracleDriver";
//        url = "jdbc:oracle:thin:@" + databaseIp + ":1521:" + databaseId;
//        username = databaseUserName;
//        password = databasePassword;
//
//        Properties properties = new Properties();
//        properties.setProperty("driverClassName", driverClass);
//        properties.setProperty("url", url);
//        properties.setProperty("username", username);
//        properties.setProperty("password", password);
//        try {
//            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
//        } catch (Exception ex) {
//            throw new RuntimeException("数据库连接失败");
//        }
//    }
//
//    public static DruidDataSource getDataSource() {
//        return dataSource;
//    }

}