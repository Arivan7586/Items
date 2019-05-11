package multi_client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 用于向服务端发送数据
 */
public class WriteDataToSeverThread extends Thread{

    private final Socket client;

    public WriteDataToSeverThread(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            try {
                OutputStream out = this.client.getOutputStream();
                PrintStream printStream = new PrintStream(out);
                System.out.print("请输入> ");
                Scanner scanner = new Scanner(System.in);
                String message = scanner.nextLine();
                printStream.println(message);
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
}
