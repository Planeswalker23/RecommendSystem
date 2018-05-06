package tool;

import entity.ClassifyArticle;
import entity.UpLoadArticle;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetArticleByType {

    public static List<UpLoadArticle> getUploadArticleByType(String type){
        List<UpLoadArticle> ls = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from articlebyuser where type = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,type);
            rs = ps.executeQuery();
            while(rs.next()){
                UpLoadArticle ua = new UpLoadArticle();
                ua.setId(rs.getInt("id"));
                ua.setTitle(rs.getString("title"));
                ua.setAuthor(rs.getString("author"));
                ua.setType(rs.getString("type"));
                ua.setContent(rs.getString("content"));
                ua.setKeyword(rs.getString("keywords"));

                long t = Long.parseLong(rs.getString("time"));
                Date date = new Date(t);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String s = df.format(date.getTime());

                ua.setTime(s);
                ls.add(ua);
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
        System.out.println(ls.size());
        return ls;
    }

    public static List<ClassifyArticle> getArticlesByType(String type){
        List<ClassifyArticle> ls = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from article_train1 where type = ?";
        int n = 0;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,type);
            rs = ps.executeQuery();
            while(rs.next()){
                ClassifyArticle ca = new ClassifyArticle();
                ca.setId(rs.getInt("id"));
                ca.setType(rs.getString("type"));
                ca.setContent(rs.getString("content"));
                ca.setTitle(rs.getString("title"));
                ls.add(ca);
                n++;
                if(n>100){
                    break;
                }
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
        System.out.println(ls.size());
        return ls;
    }

    public static void main(String[] args){
        List<ClassifyArticle> ls = getArticlesByType("culture");
        for(ClassifyArticle ca:ls){
            System.out.println(ca.getId());
            System.out.println(ca.getTitle());
            System.out.println(ca.getType());
            System.out.println(ca.getContent());
        }

    }

}
