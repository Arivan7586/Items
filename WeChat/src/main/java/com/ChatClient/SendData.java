package com.ChatClient;

import sun.java2d.loops.GraphicsPrimitive;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用于辅助客户端向服务器发送数据的工具类
 */
public class SendData implements Runnable {
    //当前客户端
    private final Socket client;

    private String receiveName;


    public SendData(Socket socket) {
        this.client = socket;
    }

    public void run() {
        while (true) {
            try {

                OutputStream out = this.client.getOutputStream();
                PrintStream printStream = new PrintStream(out);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println();
                System.out.println("注册（logon:用户名）、登陆（login:用户名）、退出（quit）、注销（logoff）、发送文件(sendFile:接收者)、私聊(private:接收者昵称:消息内容)、群聊（group:消息内容）");
                System.out.println("请输入> ");
                Scanner in = new Scanner(System.in);
                String message = in.nextLine();

                //确认接收文件
                printStream.println(message);
                if (message.startsWith("Yes")) {

                    //开始接收文件
                    sendFile();
                }

                //如果客户端输入quit，则退出客户端
                if (message.equals("quit")) {
                    this.client.close();
                    break;
                }
                printStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendFile() throws IOException {

        while (true) {
            System.out.println("请输入存储文件的空文件路径,放弃接收请输入exit：");
            Scanner in = new Scanner(System.in);

            String path = in.nextLine();

            if (path.equals("exit")) {
                break;
            }
            File file = new File(path);

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            Thread thread = new Thread(new ReceiveFile(file));
            thread.setName("receive-Thread");

            thread.start();
            break;
        }
    }
}
