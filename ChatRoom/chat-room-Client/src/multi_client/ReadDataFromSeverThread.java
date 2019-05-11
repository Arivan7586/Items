package multi_client;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用于接收服务端的数据
 */
public class ReadDataFromSeverThread extends Thread{
    private final Socket client;

    public ReadDataFromSeverThread(Socket socket) {
        this.client = socket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (this.client.isClosed()) {
                    break;
                }
                InputStream in = this.client.getInputStream();
                Scanner scanner = new Scanner(in);
                String message = scanner.nextLine();
                System.out.println("收到消息："+message);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
