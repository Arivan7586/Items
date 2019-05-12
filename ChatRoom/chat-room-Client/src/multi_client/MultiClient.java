package multi_client;


import java.io.IOException;
import java.net.Socket;

/**
 * 多线程聊天室客户端
 */
public class MultiClient {

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final Integer DEFAULT_PORT = 8888;

    public static void main(String[] args) {

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        if (args.length == 2) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("--host:")) {
                    String str  = args[i].substring("--host:".length());
                    if (checkIPv4(str)) {
                        host = str;
                    }
                } else if (args[i].startsWith("--port:")) {
                    port = Integer.parseInt(args[i].substring("--port:".length()));
                }
            }
        }

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

    /**
     * 判断给定的字符串是否为IPv4地址
     * @param str 给定字符串
     * @return 是，则返回true，否则返回false
     */
    private static boolean checkIPv4(String str) {
        String[] arr = str.split("\\.");
        if (arr.length != 4) {
            return false;
        }
        for (int i = 0; i < arr.length; i++) {
            try {
                int value = Integer.parseInt(arr[i]);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }
}
