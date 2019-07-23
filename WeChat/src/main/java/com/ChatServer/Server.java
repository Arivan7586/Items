package com.ChatServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 聊天服务器
 */
public class Server {
    //服务器默认端口
    private static final int DEFAULT_PORT = 9999;

    //用于存放客户端信息的properties文件夹
    private static String PATH = "D:\\mycode\\Items\\WeChat\\src\\main\\resources\\clientInformation.properties";

    public static void main(String[] args) {

        System.out.println("请输入服务端线程池的核心线程数：");
        Scanner scanner = new Scanner(System.in);
        int nThread = scanner.nextInt();

        //创建线程池，核心线程数5
        final ExecutorService executor = Executors.newFixedThreadPool(nThread, new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Thread-Hander-" + atomicInteger.getAndIncrement());
                return thread;
            }
        });

        //创建服务端
        try {
            ServerSocket serverSocket = new ServerSocket(DEFAULT_PORT);
            System.out.println("服务器启动完成");

            while (true) {
                final Socket socket = serverSocket.accept();

                //启动线程池
                System.out.println("客户端" + socket.getInetAddress() + "连接成功");
                executor.execute(new ServerTool(socket, PATH));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
