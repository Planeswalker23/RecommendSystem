package tool;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static recommend.DB_io.getIdByUsername;

public class JudgeFirstUse {

    public static boolean IsFirstUse(String username){
        boolean flag = true;

        Connection conn = (Connection) DBUtil.getConnection();
        int id = getIdByUsername(username);
        String sql = "select * from taste_preferences where user_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                flag = false;
                break;
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return flag;
    }

    public static void main(String[] args){
        System.out.println(IsFirstUse("aa"));
    }

}
