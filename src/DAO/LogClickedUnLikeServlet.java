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

@WebServlet("/LogClickedUnLikeServlet")
public class LogClickedUnLikeServlet extends HttpServlet {
    public static Connection conn = (Connection) DBUtil.getConnection();
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String item_id0 = request.getParameter("item_id");
        int user_id = DB_io.getIdByUsername(username);
        int item_id = Integer.parseInt(item_id0);
        DB_io.insertUnLikeItemIntoDb(item_id,user_id);
        System.out.println(user_id+":"+item_id+":"+"unlike");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
