package tool;

import entity.ClassifyArticle;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GetPopularArticles {

    public static List<ClassifyArticle> GetArticle(){
        List<ClassifyArticle> ls = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from article_train1 where id = ?";
        Random random = new Random();
        for(int i=0;i<48;i++){
            try {
                ps = conn.prepareStatement(sql);
                ps.setInt(1,random.nextInt(382));
                rs = ps.executeQuery();
                while(rs.next()){
                    ClassifyArticle ca = new ClassifyArticle();
                    ca.setId(rs.getInt("id"));
                    ca.setType(rs.getString("type"));
                    ca.setTitle(rs.getString("title"));
                    ca.setContent(rs.getString("content"));
                    ls.add(ca);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ls;
    }

}
