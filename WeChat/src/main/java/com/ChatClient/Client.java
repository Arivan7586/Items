package com.ChatClient;

import java.io.IOException;
import java.net.Socket;

/**
 * 客户端
 */
public class Client {
    //默认的localHost
    private static final String DEFAULT_HOST = "127.0.0.1";
    //默认端口号
    private static final Integer DEFAULT_PORT = 9999;


    public static void main(String[] args) {

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;

        try {
            Socket socket = new Socket(host, port);

            //创建发送数据线程
            Thread sendMessage = new Thread(new SendData(socket), "Send-Thread");
            //创建接收数据线程
            Thread receiveMessage = new Thread(new ReceiveData(socket), "Receive-Thread");

            //启动两个线程
            sendMessage.start();
            receiveMessage.start();

            System.out.println("-----------------------------欢迎使用阿怪快传----------------------------------");
            System.out.println("注册（logon:用户名）    登陆（login:用户名）     退出（quit）      注销（logoff）");
            System.out.println("发送文件(sendFile:接收者) 私聊(private:接收者昵称:消息内容) 群聊（group:消息内容）");
            System.out.println("-----------------------------------------------------------------------------");
            System.out.println();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
