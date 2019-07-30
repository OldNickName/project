package com.scand.currencies;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Properties;

class ConnectionPool {

    private static final Logger logger = Logger.getLogger(ConnectionPool.class.getName());
    private static final BasicDataSource basicDataSource = new BasicDataSource();

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = ConnectionPool.class.getClassLoader();
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(Objects.requireNonNull(classLoader.getResource("JDBCSettings.properties")).getFile());
        } catch (FileNotFoundException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        }
        try {
            properties.load(fileInputStream);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Exception: ", e);
        }
        basicDataSource.setDriverClassName(properties.getProperty("datasource.driver-class-name"));
        basicDataSource.setUrl(properties.getProperty("datasource.url"));
        basicDataSource.setUsername(properties.getProperty("datasource.username"));
        basicDataSource.setPassword(properties.getProperty("datasource.password"));
        basicDataSource.setMinIdle(5);
        basicDataSource.setMaxIdle(10);
        basicDataSource.setMaxOpenPreparedStatements(100);
    }

    static Connection getConnection() throws SQLException {
        return basicDataSource.getConnection();
    }
}
