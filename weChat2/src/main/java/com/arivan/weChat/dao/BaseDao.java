package com.arivan.weChat.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.arivan.weChat.utils.CommUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 封装对数据库的基本操作
 * 连接数据库
 * 关闭资源
 */
public class BaseDao {

    private static DataSource dataSource;

    // 获取数据源
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");

        try {
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.err.println("获取数据源失败");
            e.printStackTrace();
        }
    }

    /**
     * 连接数据库
     * @return 返回连接
     */
    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.err.println("数据库连接失败");
            e.printStackTrace();
        }
        return null;
    }

    //释放资源
    protected void closeResources(Connection connection, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //释放资源
    protected void closeResources(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(connection,statement);
        }
    }
}
