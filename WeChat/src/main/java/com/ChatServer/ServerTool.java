package com.ChatServer;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器的工具类
 */
public class ServerTool implements Runnable {
    //当前连接服务器的客户端
    private final Socket socket;

    //用于存储客户端信息的properties文件
    private final File clientMessage;

    //当前客户端的用户名
    private String clientName;

    //用于记录正在连接的客户端信息
    private static final Map<String, Socket> CLIENT_MAP = new ConcurrentHashMap<String, Socket>();

    public ServerTool(Socket socket, String path) {
        this.socket = socket;
        this.clientMessage = new File(path);
    }

    /**
     * 聊天流程
     * @throws IOException
     */
    private void chat() throws IOException {
        //接收客户端发送来的数据
        InputStream in = socket.getInputStream();
        Scanner scanner = new Scanner(in);

        while (true) {
            String message = scanner.nextLine();
            String[] value = message.split(":");
            if (value.length == 2 && value[0].equals("logon")) {
                //注册
                clientName = value[1];
                register(clientName);
            } else if (value.length == 2 && value[0].equals("login")) {
                //登陆
                clientName = value[1];
                login(clientName);
            } else if (value.length == 2 && value[0].equals("group")) {
                //群聊
                groupChat(clientName, value[1]);
            } else if (value.length == 3 && value[0].equals("private")) {
                //私聊
                String name = value[1];
                privateChat(name, value[2]);
            } else if (value[0].equals("quit")) {
                //退出聊天
                quit();
                break;
            } else if (value[0].equals("logoff")) {
                //注销账号
                logout();
            } else if (value[0].equals("sendFile")) {
                //发送文件
                String name = value[1];
                sendFile(name,message);
            } else if (message.startsWith("Yes")) {
                String name = value[1];
                privateChat(name,"sendNow");
            }
            else {
                sendMessage(this.socket,"输入格式错误！！！");
            }
        }
    }

    public void run() {

        try {
            chat();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @param name 文件接收者
     */
    private void sendFile(String name,String message) {
        if (!CLIENT_MAP.containsValue(this.socket)) {
            sendMessage(this.socket,"请先登陆(login:用户名)！！！");
        } else {
            if (CLIENT_MAP.containsKey(name)) {
                Socket socket = CLIENT_MAP.get(name);
                String[] str = message.trim().split(":");
                String fileName = str[str.length-1];
                sendMessage(socket, clientName + "想给您发送文件，接收，请输入Yes:用户名，否则输入No:用户名");
            } else {
                Properties properties = new Properties();
                try {
                    properties.load(new FileInputStream(this.clientMessage));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (properties.containsKey(name)) {
                    sendMessage(this.socket,"该用户不在线");
                } else {
                    sendMessage(this.socket,"该用户不存在");
                }
            }
        }
    }

    /**
     * 注销当前用户
     */
    private void logout() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.clientMessage));
            properties.remove(clientName);
            properties.store(new FileOutputStream(this.clientMessage),"删除账户信息");
            quit();
            sendMessage(this.socket,"用户<"+clientName+">已被成功注销");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登陆
     * @param name 待登陆的用户名
     */
    private void login(String name) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.clientMessage));
            if (beUsed(name)) {
                CLIENT_MAP.put(name,this.socket);
                sendMessage(this.socket,"登陆成功");
                clientCount();
            } else {
                sendMessage(this.socket,"当前账号不存在，请核对或注册！！！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 退出聊天
     */
    private void quit() {
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
        if (CLIENT_MAP.containsKey(name)) {
            Socket socket = CLIENT_MAP.get(name);
            sendMessage(socket, clientName + "说：" + s);
        } else {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(this.clientMessage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (properties.containsKey(name)) {
                sendMessage(this.socket,"该用户不在线");
            } else {
                sendMessage(this.socket,"该用户不存在");
            }
        }

    }

    /**
     * 给除了发消息的客户端以外，所有的客户端发消息（群聊）
     *
     * @param sender 消息的发送者
     * @param s      消息内容
     */
    private void groupChat(String sender, String s) {
        if (!CLIENT_MAP.containsValue(this.socket)) {
            sendMessage(this.socket,"请先登陆(login:用户名)！！！");
        } else {
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
    }

    /**
     * 注册聊天室
     *
     * @param name 用户名
     */
    private void register(String name) {
        //注册聊天室
        if (beUsed(name)) {
            sendMessage(this.socket,"注册失败（该用户名已存在）");
        } else {
            Properties properties = new Properties();
            try {
                properties.load(new FileInputStream(this.clientMessage));
                properties.setProperty(name, String.valueOf(this.socket));
                properties.store(new FileOutputStream(this.clientMessage),"注册新用户");
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            System.out.println("当前在线" + set.size() + "人，用户如下：");
            System.out.print("(");
            for (String s : set) {
                System.out.print(s + " ");
            }
            System.out.print(")");
        }
        System.out.println();
    }

    /**
     * 判断当前用户名是否已经存在
     * @param name 被判断的用户名
     * @return 存在返回true，否则返回false
     */
    private boolean beUsed(String name) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(this.clientMessage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.containsKey(name);
    }
}
