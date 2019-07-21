package com.ChatClient;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ReceiveFile extends Thread {

    //目标文件夹
    private final File file;

    public ReceiveFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {

        //启动服务端
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8888);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            try {

                System.out.println("开始接收");
                Socket socket = serverSocket.accept();

                //接收文件
                InputStream in = socket.getInputStream();

                OutputStream out = new FileOutputStream(file);

                int length = 0;

                byte[] arr = new byte[1024*2];

                while ((length = in.read(arr)) != -1) {
                    out.write(arr,0,length);
                }
                System.out.println("接收完成");
                in.close();
                out.close();
                serverSocket.close();
                break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
