package tool;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetTitleById {
    public static Connection conn = (Connection) DBUtil.getConnection();

    public static String getTitle(int id){
        String title = "";
        try{
            String sql = " select title from article where id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                title = rs.getString("title");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return title;
    }
}
