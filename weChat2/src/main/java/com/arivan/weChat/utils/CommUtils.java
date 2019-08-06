package com.arivan.weChat.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 基础的工具类，封装了基础的工具方法
 * 包括加载配置文件properties文件的方法
 * Gson序列化方法
 * Gson反序列化方法
 */
public class CommUtils {

    private static final Gson gson =
            new GsonBuilder().create();

    private CommUtils() {}

    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        // 获取当前配置文件夹下的文件输入流
        InputStream in = CommUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        // 加载配置文件中的所有内容
        try {
            properties.load(in);
            in.close();
        } catch (IOException e) {
            System.err.println("加载数据源配置文件失败");
            e.printStackTrace();
        }
        return properties;
    }

    /**
     * 将给定类序列化为String类型的字符串
     * @param object 给定的object类
     * @return 返回序列化后的字符串
     */
    public static String ObjectToGson(Object object) {
        return gson.toJson(object);
    }

    /**
     * 将给定Gson字符串转换为指定的类
     * @param gsonStr 给定的Gson字符串
     * @param objClass 指定的类
     * @return
     */
    public static Object gsonToObject(String gsonStr, Class objClass) {
        return gson.fromJson(gsonStr,objClass);
    }

    /**
     * 判断给定字符串是否为空
     * @param string 给定字符串
     * @return 如果为空，返回true，反之返回false
     */
    public static boolean strIsNull(String string) {
        return string == null || string.equals("");
    }
}
