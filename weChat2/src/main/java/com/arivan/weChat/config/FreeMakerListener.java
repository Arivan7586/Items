package com.arivan.weChat.config;

import freemarker.template.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebListener
public class FreeMakerListener implements ServletContextListener {
    public static final String TEMPLATE_KEY = "_template_";

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //配置版本
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

        // 配置加载ftl的路径
        try {
            configuration.setDirectoryForTemplateLoading(
                    new File("D:\\mycode\\Items\\weChat2\\src\\main\\webapp"));
        } catch (IOException e) {
            System.err.println("配置ftl的加载路径失败");
            e.printStackTrace();
        }

        configuration.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        servletContextEvent.getServletContext().setAttribute(TEMPLATE_KEY,configuration);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
