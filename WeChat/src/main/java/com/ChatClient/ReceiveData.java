package com.ChatClient;

import java.awt.datatransfer.FlavorEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用于辅助客户端接收服务端数据的工具类
 */
public class ReceiveData implements Runnable {
    //当前客户端
    private final Socket client;

    public ReceiveData(Socket socket) {
        this.client = socket;
    }

    public void run() {
        //客户端不关闭，此工具类一直运行
        while (true) {
            if (this.client.isInputShutdown()) {
                break;
            }
            try {
                InputStream in = this.client.getInputStream();
                Scanner scanner = new Scanner(in);
                String message = scanner.nextLine();
                if (message.endsWith("sendNow")) {
                    //开始发送文件
                    send();
                } else {
                    System.out.println("收到消息>" + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void send() {
        while (true) {
            System.out.println("请输入要发送的文件路径,放弃发送请输入exit：");
            Scanner in = new Scanner(System.in);
            String path = in.next();
            if (path.equals("exit")) {
                break;
            }
            File file = new File(path);

            if (!file.isFile()) {
                System.out.println("路径不正确");
            } else {

                Thread thread = new Thread(new SendFile(file));
                thread.setName("sendFile-Thread");
                thread.start();
                break;
            }
        }
    }

}
