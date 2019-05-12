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

    private static Integer DEFAULT_THREAD_COUNT = 10;
    private static Integer DEFAULT_PORT = 8888;

    public static void main(String[] args) {
        int nThread = DEFAULT_THREAD_COUNT;
        int port = DEFAULT_PORT;

        //如果没有输入线程数和port，就采用默认数值
        if (args.length == 2) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("--port:")) {
                    port = Integer.parseInt(args[i].substring("--port".length()));
                } else if (args[i].startsWith("--nThread:")) {
                    nThread = Integer.parseInt(args[i].substring("--nThread".length()));
                }
            }
        }

        final ExecutorService executor = Executors.newFixedThreadPool(nThread, new ThreadFactory() {
            private final AtomicInteger atomicInteger = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("Thread-Handler-" + atomicInteger.getAndIncrement());
                return thread;
            }
        });


        try {
            ServerSocket serverSocket = new ServerSocket(port);
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
