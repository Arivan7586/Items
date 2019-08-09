package com.arivan.weChat.service;


import com.arivan.weChat.entity.MessageFromClient;
import com.arivan.weChat.entity.MessageToClient;
import com.arivan.weChat.utils.CommUtils;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * Service层，负责聊天功能的具体实现
 */
@ServerEndpoint("/webSocket")
public class ChatService {

    //存储所有连接到后端的webSocket

    private static CopyOnWriteArraySet<ChatService> clients =
            new CopyOnWriteArraySet<>();

    //用于缓存所有用户的列表
    public static Map<String,String> userNames =
            new ConcurrentHashMap<>();

    //绑定当前webSocket会话
    private Session session;

    //记录当前客户端的用户名
    private String userName;

    @OnOpen
    public void onOpen(Session session) {
        //获取会话
        this.session = session;

        //获取当前客户端的用户名
        this.userName = this.session.getQueryString().split("=")[1];
        // 将客户端聊天实体保存到clients
        clients.add(this);
        // 将当前用户以及SessionID保存到用户列表
        userNames.put(session.getId(),this.userName);
        System.out.println("有新的连接,SessionID为"+session.getId() + ",用户名为"+userName);

        // 发送给所有在线用户一个上线通知
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName + "上线了");
        messageToClient.setNames(userNames);

        //发送消息
        String message = CommUtils.ObjectToGson(messageToClient);
        for (ChatService chatService : clients) {
            chatService.sendMessage(message);
        }
    }

    @OnError
    public void onError(Throwable throwable) {
        System.err.println("WebSocket连接失败！");
    }

    @OnMessage
    public void onMessage(String message) {
        //将收到的消息字符串，Gson反序列化为MessageFromClient类
        MessageFromClient messageFromClient =
                (MessageFromClient) CommUtils.gsonToObject(message,MessageFromClient.class);

        //执行群聊
        if (messageFromClient.getType().equals("1")) {
            //解析收到的消息
            String content = messageFromClient.getMsg();

            //包装消息内容
            MessageToClient messageToClient = new MessageToClient();
            messageToClient.setContent(content);
            messageToClient.setNames(userNames);

            //广播发送
            for (ChatService chatService : clients) {
                chatService.sendMessage(CommUtils.ObjectToGson(messageToClient));
            }
        } else if (messageFromClient.getType().equals("2")) {
            //执行私聊
            //解析消息内容
            String content = messageFromClient.getMsg();
            int toLength = messageFromClient.getTo().length();
            String[] users = messageFromClient.getTo()
                    .substring(0,toLength-1).split("-");
            List<String> list = Arrays.asList(users);

            for (ChatService chatService : clients) {
                String sessionId = chatService.session.getId();
                if (list.contains(sessionId) &&
                !this.session.getId().equals(sessionId)) {
                    //发送消息

                    //包装消息内容
                    MessageToClient messageToClient = new MessageToClient();
                    messageToClient.setContent(userName,content);
                    messageToClient.setNames(userNames);

                    chatService.sendMessage(CommUtils.ObjectToGson(messageToClient));

                }
            }
        }
    }

    @OnClose
    public void onClose() {
        //移除当前客户端
        clients.remove(this);
        //将当前用户从用户列表移除
        userNames.remove(session.getId());
        System.out.println("有连接下线了"+
                ",用户名为"+userName);

        // 发送给所有在线用户一个下线通知

        //包装消息内容
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName + "下线了！");
        messageToClient.setNames(userNames);

        //发送消息
        String message = CommUtils.ObjectToGson(messageToClient);
        for (ChatService chatService : clients) {
            chatService.sendMessage(message);
        }
    }

    /**
     * 发送消息
     * @param message 消息内容
     */
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("发送消息失败");
            e.printStackTrace();
        }
    }
}
