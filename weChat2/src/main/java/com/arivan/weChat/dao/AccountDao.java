package com.arivan.weChat.dao;

import com.arivan.weChat.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

/**
 * 实现用户信息与数据库交互的具体实现类
 */
public class AccountDao extends BaseDao{

    /**
     * 查询数据库中是否存在给定用户
     * @param userName 给定用户的用户名
     * @param password 给定用户的密码
     * @return 如果存在给定用户，返回封装后的用户信息，若不存在，则返回null
     */
    public User selectUser(String userName, String password) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        User user = null;

        // 1、获取数据库连接
        connection = getConnection();

        // 2、查询语句
        String sql = "select * from user where userName = ? " +
                "and password = ?";

        //3、执行查询语句
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1,userName);
            //使用mh5加密方式存储用户密码
            statement.setString(2, DigestUtils.md5Hex(password));

            //4、接收查询结果集
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
            }
        } catch (SQLException e) {
            System.err.println("查询用户信息出错");
            e.printStackTrace();
        } finally {
            //5、关闭数据源
            closeResources(connection,statement,resultSet);
        }
        return user;
    }

    /**
     * 向数据库添加新用户
     * @param user 用户信息
     * @return 返回添加的结果
     */
    public boolean addUser(User user) {
        String userName = user.getUserName();
        String password = user.getPassword();
        if (selectUser(userName,password) == null) {
            //说明该用户不存在，可以创建
            Connection connection = null;
            PreparedStatement statement = null;
            boolean isSuccessful = false;

            connection = getConnection();
            String sal = "INSERT into user (username,password) values (?,?)";
            try {
                statement = connection.prepareStatement(sal);
                statement.setString(1,userName);
                statement.setString(2,DigestUtils.md5Hex(password));

                //插入成功，返回true
                isSuccessful =  (statement.executeUpdate() == 1);
            } catch (SQLException e) {
                System.err.println("插入用户信息出错");
                e.printStackTrace();
            } finally {
                closeResources(connection,statement);
            }
            return isSuccessful;
        } else {
            //说明该用户已存在，创建失败
            return false;
        }
    }

    /**
     * 将ResultSet结果集中的用户信息封成User类
     * @param resultSet
     * @return 返回封装后的User实例
     */
    private User getUser(ResultSet resultSet) {
        User user = new User();
        try {
            user.setId(resultSet.getInt("id"));
            user.setUserName(resultSet.getString("userName"));
            user.setPassword(resultSet.getString("password"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
}
