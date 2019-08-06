package com.arivan.weChat.controller;

import com.arivan.weChat.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 与浏览器交互，实现新用户注册
 */

@WebServlet(urlPatterns = "/doRegister")
public class RegisterController extends HttpServlet {

    private AccountService accountService = new AccountService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       String userName = request.getParameter("username");
       String password = request.getParameter("password");

       response.setContentType("text/html;charset=utf8");
       PrintWriter writer = response.getWriter();

       String res = "";
       if (accountService.userRegister(userName,password)) {
           //新用户注册成功
           res = "\"注册成功\"";
       } else {
           //新用户注册失败
           res = "\"注册失败\"";
       }

        writer.println("<script>\n" +
//                "    alert(\"注册成功\");\n" +
                "    alert(" + res + ");\n" +
                "    window.location.href = \"/index.html\";\n" +
                "</script>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req,resp);
    }
}
