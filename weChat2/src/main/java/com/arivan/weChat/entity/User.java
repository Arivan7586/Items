package com.arivan.weChat.entity;

import lombok.Data;

/**
 * 封装用户信息
 */

@Data
public class User {

    //用户ID
    private Integer id;
    //用户名
    private String userName;
    //用户密码
    private String password;

    public User() {
    }

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

}
