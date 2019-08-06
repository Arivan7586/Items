package com.arivan.weChat.entity;

import lombok.Data;

import java.util.Map;

/**
 * 后端给前端发送的消息实体
 */

@Data
public class MessageToClient {

    //消息内容
    private String content;

    //当前在线的所有用户的用户名
    private Map<String,String> names;

    public void setContent(String msg) {
        this.content = msg;
    }

    public void setContent(String userName, String msg) {
        this.content = userName + "说> " + msg;
    }
}
