<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/4/5
  Time: 18:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>logout</title>
    <link rel="shortcut icon" href="images/favicon.png" />
</head>
<body background="images/footer-bg.png">
<div class="search-area-wrapper" style="text-align: center;border-radius: 20px;width: 300px;height: 350px;margin: auto;position: absolute;top: 0;left: 0;right: 0;bottom: 0;">
    <div class="search-area container">
        <h1 style="color: whitesmoke;font-style:italic">注销成功</h1>
        <%
            session.setAttribute("username","");
        %>
        <a href="login.jsp"><h2 style="color: #f0b70c;font-style:italic">重新登录</h2></a>
        <a href="articles-list.jsp"><h2 style="color: #f0b70c;font-style:italic">回到主页</h2></a><br>
    </div>
</div>

</body>
</html>
