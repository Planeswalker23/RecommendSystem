package recommend;

import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

public class Filter {

    public static boolean FilterByReadTime(int user_id,int item_id){
        boolean flag = false;
        Connection conn = (Connection) DBUtil.getConnection();
        String sql0 = "select * from readtime where user_id = ? and item_id = ?";
        Timestamp beginTime = null;
        Timestamp endTime = null;
        try {
            PreparedStatement ps = conn.prepareStatement(sql0);
            ps.setInt(1, user_id);
            ps.setInt(2,item_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                beginTime = rs.getTimestamp("read_begin");
                endTime = rs.getTimestamp("read_end");
            }
//            System.out.println(beginTime.getTime());
//            System.out.println(endTime.getTime());
//            System.out.println(endTime.getTime()/1000-beginTime.getTime()/1000);
//            if((endTime.getTime()/1000-beginTime.getTime()/1000)<10){
//                flag = true;
//            }
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static boolean FilterByHate(int user_id,int item_id){

        boolean flag = false;
        Connection conn = (Connection) DBUtil.getConnection();
        String sql = "select * from article_hate where user_id = ? and item_id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setInt(2,item_id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
               flag = true;
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    public static void main(String[] args){
        System.out.println(FilterByHate(0,904));
    }

}
