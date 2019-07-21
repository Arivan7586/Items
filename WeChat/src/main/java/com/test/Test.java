package com.test;

import com.ChatClient.ReceiveFile;
import com.ChatClient.SendFile;

import java.awt.datatransfer.FlavorEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Test {

    private static String run(String path) {
        String[] arr = path.trim().split("\\\\");
        String sb = "";
        for (int i = 0; i < arr.length; i++) {
            sb += arr[i];
            if (i != arr.length-1) {
                sb += File.separator;
            }
        }
        return sb;
    }

    public static void main(String[] args) {

        String value = "D:\\迅雷下载\\录屏\\10_31.wmv";

        String s = "D:\\7z\\Java多态详解\\新建文件夹\\qiaoben\\[44x.me]SNIS-696-C.mp4"; // 1.15g

        // D:\\迅雷下载\\冰与火之歌 权力的游戏 第二季\\Game.of.Thrones.S02E01.2012.BluRay.1080p.HEVC.AC3.中英特效.mp4

        //   ‪C:\Users\11940\Desktop\简历.pdf   D:\迅雷下载\receive\aaaa    C:/Users/11940/Desktop/简历.pdf
        // D:\mycode\Items\WeChat\src\main\java\com\test\Test.java
        //  "D:\\迅雷下载\\冰与火之歌 权力的游戏 第二季\\Game.of.Thrones.S02E01.2012.BluRay.1080p.HEVC.AC3.中英特效.mp4"
        //  "D:\迅雷下载\冰与火之歌 权力的游戏 第二季\Game.of.Thrones.S02E01.2012.BluRay.1080p.HEVC.AC3.中英特效.mp4"

       // ‪C:\\Users\\11940\\Desktop\\简历.pdf
      //  D:\\迅雷下载\\receive\\aaa

        // ‪D:\迅雷下载\录屏\10_31.wmv

        Scanner in = new Scanner(System.in);

        System.out.println("请输入目标路径");
        String target = in.nextLine();

        File f4 = new File(target);

        if (!f4.getParentFile().exists()) {
            f4.getParentFile().mkdirs();
        }

        Thread thread2 = new Thread(new ReceiveFile(f4));

        thread2.start();



        System.out.println("请输入源路径");
        String source = in.nextLine();


//        File file1 = new File(source);
//        File file2 = new File(target);



        File f3 = new File(source);

//        System.out.println(f3.exists());
//        System.out.println(f4.exists());
//        System.out.println("-----------");
//
//        System.out.println(f3.isDirectory());
//        System.out.println(f4.isDirectory());
//
//        System.out.println("-----------");
//        System.out.println(f3.isFile());
//        System.out.println(f4.isFile());
//



        Thread thread1 = new Thread(new SendFile(f3));


        thread1.start();
    }
}
