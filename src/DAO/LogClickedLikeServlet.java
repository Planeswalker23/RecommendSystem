package DAO;

import recommend.DB_io;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/LogClickedLikeServlet")
public class LogClickedLikeServlet extends HttpServlet {
    public static Connection conn = (Connection) DBUtil.getConnection();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String item_id0 = request.getParameter("item_id");
        int item_id = Integer.parseInt(item_id0);
        int user_id = DB_io.getIdByUsername(username);
        DB_io.insertLikeItemIntoDb(item_id,user_id);
        System.out.println(user_id+":"+item_id+":"+"like");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
