package tool;

import entity.ClassifyArticle;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetDetailContent {

    public static ClassifyArticle getContent(int id){
        ClassifyArticle ca = new ClassifyArticle();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from article_train1 where id = ?";
        int n = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while(rs.next()){
                ca.setId(rs.getInt("id"));
                ca.setType(rs.getString("type"));
                ca.setTitle(rs.getString("title"));
                ca.setContent(rs.getString("content"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return ca;
    }

}
