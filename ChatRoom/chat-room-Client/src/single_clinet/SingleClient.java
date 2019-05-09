package single_clinet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端（单线程）
 */
public class SingleClient {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 6666;
        try {
            // 1 创建客户端Socket和服务器的连接
            final Socket client = new Socket(host,port);

            //2 客户端进行数据的接收和发送
            //2.1 客户端发送数据(Write)
            OutputStream out = client.getOutputStream();
            PrintStream printStream = new PrintStream(out);
            System.out.println("请输入需要发送的消息：");
            Scanner scanner1 = new Scanner(System.in);
            printStream.println(scanner1.nextLine());
            printStream.flush();

            //2.2 客户端接收数据
            InputStream in = client.getInputStream();
            Scanner scanner = new Scanner(in);
            String message = scanner.nextLine();
            System.out.println("从服务器接收到的数据："+message);
        } catch (IOException e) {

        }
    }
}
