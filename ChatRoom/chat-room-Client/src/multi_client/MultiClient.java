package multi_client;


import java.io.IOException;
import java.net.Socket;

/**
 * 多线程聊天室客户端
 */
public class MultiClient {

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 8888;

        try {
            Socket client = new Socket(host,port);
            Thread read = new ReadDataFromSeverThread(client);
            read.setName("Read-Thread");
            Thread write = new WriteDataToSeverThread(client);
            write.setName("Write-Thread");

            read.start();
            write.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
