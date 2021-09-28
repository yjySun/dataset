package com.yjy.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.sql.SQLException;
import java.util.Properties;

/**
 * 编写JDBC工具类，获取数据库连接
 * 读取配置文件，获取连接，执行一次，
 */
public class DBUtil {
    private static String driverClass;
    private static String url;
    private static String username;
    private static String password;
    private static DruidDataSource dataSource;

    static {
        try {
            driverClass = "oracle.jdbc.OracleDriver";
            url = "jdbc:oracle:thin:@172.0.17.30:1521:MARSDB";
            username = "MARSDB";
            password = "admin123";
            Properties properties = new Properties();
            properties.setProperty("driverClassName", driverClass);
            properties.setProperty("url", url);
            properties.setProperty("username", username);
            properties.setProperty("password", password);

            dataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception ex) {
            throw new RuntimeException("数据库连接失败");
        }

    }

    public static DruidDataSource getDataSource() throws SQLException {
        return dataSource;
    }

}