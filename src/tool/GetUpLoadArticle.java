package tool;

import entity.UpLoadArticle;
import org.junit.Test;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetUpLoadArticle {

    @Test
    public static List<UpLoadArticle> getUpLoadArticle(){

        List<UpLoadArticle> ls = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from articlebyuser";
        try {
            ps = conn.prepareStatement(sql);
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
        //System.out.println(ls.size());
        return ls;

    }

    public static UpLoadArticle getUpLoadArticlefromDb(int id){
        UpLoadArticle ua = new UpLoadArticle();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        conn = (Connection) DBUtil.getConnection();
        String sql = "select * from articlebyuser where id = ?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            rs = ps.executeQuery();
            while(rs.next()){
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

        return ua;
    }

}
