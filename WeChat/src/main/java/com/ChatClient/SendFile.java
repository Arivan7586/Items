package com.ChatClient;

import java.io.*;
import java.net.Socket;

/**
 * 负责发送文件
 */

public class SendFile extends Thread {
    //源文件
    private final File file;

    public SendFile(File file) {
        this.file = file;
    }

    @Override
    public void run() {

        try {

            //新建的发送端
            Socket socket = new Socket("127.0.0.1", 8888);

            OutputStream out = socket.getOutputStream();

            //源文件输入流
            InputStream in = new FileInputStream(file);

            long start = System.currentTimeMillis();
            System.out.println("开始发送");

            //将数据读入数组中
            byte[] arr = new byte[1024 * 2];

            int len = 0;
            while ((len = in.read(arr)) != -1) {
                out.write(arr, 0, len);
            }
            in.close();
            out.close();

            long end = System.currentTimeMillis();
            System.out.println("发送完成,耗时：" + (end - start) + "毫秒");

            //文件传输完成，关闭Socket
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
