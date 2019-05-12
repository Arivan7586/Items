package multi_server;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接收所有客户端的数据，并作出相应的处理和反馈
 */
public class ClientHandler implements Runnable {

    //使用map来保存所有客户端的信息
    private static final Map<String, Socket> CLIENT_MAP = new ConcurrentHashMap<>();

    private final Socket socket; //当前访问服务器的客户端

    private String clientName = ""; //用于记录客户端的用户名

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            InputStream in = socket.getInputStream();
            Scanner scanner = new Scanner(in);

            while (true) {
                String message = scanner.nextLine();
                String[] value = message.split(":");
                if (value.length == 2 && value[0].equals("user")) {
                    //注册
                    clientName = value[1];
                    register(clientName);
                } else if (value.length == 2 && value[0].equals("group")) {
                    //群聊
                    groupChat(clientName, value[1]);
                } else if (value.length == 3 && value[0].equals("private")) {
                    //私聊
                    String name = value[1];
                    privateChat(name, value[2]);
                } else if (value[0].equals("quit")) {
                    //退出
                    quit();
                    break;
                } else {
                    sendMessage(this.socket,"输入格式错误！！！");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出聊天
     */
    private void quit() {
        //退出聊天室
        CLIENT_MAP.remove(clientName);
        Iterator<Socket> iterator = CLIENT_MAP.values().iterator();
        while (iterator.hasNext()) {
            Socket socket = iterator.next();
            sendMessage(socket, clientName + "已下线！！！");
        }
        clientCount();
    }

    /**
     * 给指定的客户端发消息（私聊）
     *
     * @param name 目标客户端
     * @param s    消息内容
     */
    private void privateChat(String name, String s) {
        Socket socket = CLIENT_MAP.get(name);
        sendMessage(socket, clientName + "说> " + s);
    }

    /**
     * 给除了发消息的客户端以外，所有的客户端发消息（群聊）
     *
     * @param sender 消息的发送者
     * @param s      消息内容
     */
    private void groupChat(String sender, String s) {
        Iterator<Map.Entry<String, Socket>> iterator = CLIENT_MAP.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Socket> entry = iterator.next();
            String name = entry.getKey();
            Socket socket = entry.getValue();
            if (name != sender) {
                sendMessage(socket, sender + "说> " + s);
            }
        }
    }

    /**
     * 注册聊天室
     *
     * @param name 用户名
     */
    private void register(String name) {
        //注册聊天室
        if (CLIENT_MAP.containsKey(name)) {
            sendMessage(this.socket,"注册失败（该用户名已存在）");
        } else {
            CLIENT_MAP.put(name, this.socket);
            sendMessage(this.socket, "恭喜<" + name + ">注册成功");
            clientCount();
        }
    }

    /**
     * 给指定的客户端发送信息
     *
     * @param socket  目标客户端
     * @param message 消息内容
     */
    private void sendMessage(Socket socket, String message) {
        try {
            OutputStream out = socket.getOutputStream();
            PrintStream printStream = new PrintStream(out);
            printStream.println(message);
            printStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 统计当前聊天室的用户情况
     */
    private void clientCount() {
        Set<String> set = CLIENT_MAP.keySet();
        if (!set.isEmpty()) {
            System.out.println("聊天室当前在线" + set.size() + "人，用户如下：");
            System.out.print("(");
            for (String s : set) {
                System.out.print(s + " ");
            }
            System.out.print(")");
        }
        System.out.println();
    }
}
