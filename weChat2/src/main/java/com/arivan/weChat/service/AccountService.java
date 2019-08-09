package com.arivan.weChat.service;

import com.arivan.weChat.dao.AccountDao;
import com.arivan.weChat.entity.User;

import java.util.Map;

/**
 * Service层，负责处理用户的注册、登录请求
 */
public class AccountService {

    private AccountDao accountDao = new AccountDao();

    /**
     * 用户登录
     * @param userName 用户名
     * @param password 用户密码
     * @return 登陆成功返回true，否则返回false
     */
    public boolean userLogin(String userName, String password) {
        User user = accountDao.selectUser(userName,password);
        return user != null;
    }

    /**
     * 新用户注册
     * @param userName 用户名
     * @param password 用户密码
     * @return
     */
    public boolean userRegister(String userName,String password) {
        return accountDao.addUser(new User(userName,password));
    }

    /**
     *判断当前用户是否已经登录
     * @param username 当前用户名
     * @return 在线，返回true，否则返回false
     */
    public boolean checkIsOnline(String username) {
        Map<String,String> map = ChatService.userNames;
        return map.containsValue(username);
    }

    /**
     * 判断当前用户的用户名是否存在
     * @param username 当前用户的用户名
     * @return 存在返回true，否则返回false
     */
    public boolean userNameIsExist(String username,String password) {
        return new AccountDao().selectUser(username,password) != null;
    }
}
