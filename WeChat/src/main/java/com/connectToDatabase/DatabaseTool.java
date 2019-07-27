package com.connectToDatabase;

import java.sql.*;

/**
 * 用于对用户信息操作的类
 */
public class DatabaseTool {

    private DatabaseTool() {}

    public static void main(String[] args) {

        System.out.println(selectUser("jack"));

//        System.out.println(addUser("jack"));

        deleteUser("jack");

        System.out.println(selectUser("jack"));
    }

    //加载数据库驱动
    private static void load() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //连接数据库
    private static Connection connect() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/wechat1", "root","wwe15991185490");
    }

    //关闭数据库连接
    private static void close(Connection connection, PreparedStatement preparedStatement,
                              ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 查询用户是否存在
     * @param name 给定用户名
     * @return 存在该用户，返回true
     */
    public static boolean selectUser(String name) {
        if (name == null) {
            return false;
        }
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            //加载MySQL驱动
            load();

            //连接数据库
            connection = connect();

            //查询语句
            String selectSQL = "select userName from user";
            preparedStatement = connection.prepareStatement(selectSQL);

            //记录结果集
            resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                return false;
            }

            while (resultSet.next()) {
                String userName = resultSet.getString(1);
                if (userName.equals(name)) {
                    return true;
                }
            }
            return false;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //断开连接
            close(connection,preparedStatement,resultSet);
        }
        return false;
    }


    /**
     * 向数据库添加用户
     * @param name 用户名
     * @return 添加成功返回true，否则返回false
     */
    public static boolean addUser(String name) {
        if (name == null) {
            return false;
        }

        //当前数据库已经存在该用户
        if (selectUser(name)) {
            return false;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connect();

            String sql = "insert into user (userName) values (?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);

            preparedStatement.execute();

            return selectUser(name);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection,preparedStatement,null);
        }
    }


    public static void deleteUser(String name) {
        if (name == null) {
            return;
        }
        load();
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connect();
            String sql = "delete from user where userName=?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,name);

            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
