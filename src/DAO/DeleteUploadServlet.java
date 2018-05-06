package DAO;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/DeleteUploadServlet")
public class DeleteUploadServlet extends HttpServlet {

    public static int index = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id0 = request.getParameter("item_id");
        int id = Integer.parseInt(id0);
        Connection conn = null;
        PreparedStatement ps = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "delete from articlebyuser where id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("删除" + id);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

}

    public static int getPage(){
        return index;
    }
}
