package single_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * 聊天室服务器（单线程）
 */
public class SingleServer {

    public static void main(String[] args) {
        try {
            // 1创建ServerSocket
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("服务器启动 "+serverSocket.getInetAddress()
            + ":" + serverSocket.getLocalPort());

            //2 等待客户端连接（阻塞操作）
            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("客户端连接：" +
                                    socket.getRemoteSocketAddress() + ":" +
                                    socket.getPort());
                            //3 服务器端可以进行接收数据和发送数据
                            //3.1 服务器端接收数据
                            InputStream in = socket.getInputStream();
                            Scanner scanner = new Scanner(in);
                            String message = scanner.nextLine();
                            System.out.println("接收到客户端的数据："+message);

                            //3.2 服务器发送数据
                            OutputStream out = socket.getOutputStream();
                            PrintStream printStream = new PrintStream(out);
                            printStream.println("你好客户端");
                            printStream.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        } catch (IOException e) {
            System.out.println("服务端发生异常：" + e.getMessage());
        }
    }
}