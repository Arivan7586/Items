package multi_server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 多线程聊天室服务端
 */
public class MultiServer {

    private static final ExecutorService executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
        private final AtomicInteger atomicInteger = new AtomicInteger(0);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            thread.setName("Thread-Handler-" + atomicInteger.getAndIncrement());
            return thread;
        }
    });
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("服务器启动" + serverSocket.getInetAddress() + ":" + serverSocket.getLocalPort());

            while (true) {
                final Socket socket = serverSocket.accept();
                //启动线程池
                System.out.println("当前客户端："+socket.getInetAddress());
                executor.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
