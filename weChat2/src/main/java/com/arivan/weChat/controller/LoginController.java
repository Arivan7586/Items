package com.arivan.weChat.controller;


import com.arivan.weChat.config.FreeMakerListener;
import com.arivan.weChat.service.AccountService;
import com.arivan.weChat.utils.CommUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 与浏览器交互，实现用户登录
 */
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {

    private AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");

        response.setContentType("text/html;charset=utf8");
        PrintWriter out = response.getWriter();

        if (CommUtils.strIsNull(userName) || CommUtils.strIsNull(password)) {
            //用户名或者密码不合法，
            write(out,"\"用户名或密码为空!\"");
        } else {
            //用户名与密码格式合法，执行登录流程
            if (accountService.userLogin(userName,password)) {
                //登录成功,跳转到聊天页面
                //加载chat.ftl
                Template template = getTemplate(request,"/chat.ftl");

                Map<String,String> map = new ConcurrentHashMap<>();

                map.put("username",userName);

                try {
                    template.process(map,out);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            } else {
                write(out,"\"用户名或密码不正确!\"");
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }

    //加载chat.ftl文件
    private Template getTemplate(HttpServletRequest request, String fileName) {
        Configuration configuration = (Configuration) request.getServletContext()
                .getAttribute(FreeMakerListener.TEMPLATE_KEY);
        try {
            return configuration.getTemplate(fileName);
        } catch (IOException e) {
            System.err.println("加载chat.ftl文件出错");
            e.printStackTrace();
        }
        return null;
    }

    //在页面弹出提示信息
    private void write(PrintWriter writer, String value) {
        writer.println("    <script>\n" +
                "        alert(" + value + ");\n" +
                "        window.location.href = \"/index.html\";\n" +
                "    </script>");
    }
}
