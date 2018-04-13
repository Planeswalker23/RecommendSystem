package recommend;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DB_io {
    public static Connection conn = (Connection) DBUtil.getConnection();

    public static void insertReadTimeIntoDB(int user_id,int item_id,String flag)
    {
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        boolean notInDb = true;
        Timestamp ts_old = null;
        String sql2 = "select * from readtime where item_id = ? and user_id = ?";
        PreparedStatement ps2 = null;
        try {
            ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1,item_id);
            ps2.setInt(2,user_id);
            ResultSet rs2 = ps2.executeQuery();
            while(rs2.next()){
                notInDb = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

       if(flag.equals("begin")&&notInDb) {
           String sql = "insert into readtime values(?,?,?,?)";
           try {
               PreparedStatement ps = conn.prepareStatement(sql);
               ps.setInt(1,user_id);
               ps.setInt(2,item_id);
               ps.setTimestamp(3,ts);
               ps.setTimestamp(4,null);
               ps.execute();
              System.out.println(user_id+":"+item_id+":"+flag+":"+ts);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }else if(flag.equals("begin")&&(!notInDb)){

       }
       else if(flag.equals("end")){
           String sql = "update readtime set read_end = ? where user_id=? and item_id = ?";
           try {
               PreparedStatement ps = conn.prepareStatement(sql);
               ps.setTimestamp(1,ts);
               ps.setInt(2,user_id);
               ps.setInt(3,item_id);

               ps.execute();
               System.out.println(user_id+":"+item_id+":"+flag+":"+ts);
           } catch (SQLException e) {
               e.printStackTrace();
           }
       }
    }

    public static long[] getItemIdFromDB()
    {
        List<Long> ids = new ArrayList();


        String sql = "select distinct item_id from taste_preferences  order by item_id";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                long id = rs.getInt("item_id");
                ids.add(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        long[] items = ids.stream().mapToLong(t->t.longValue()).toArray();

        return items;

    }

    public static void deleteSimilarityIntoDb(){
        String sql0 = "delete from taste_item_similarity";
        try {
            PreparedStatement ps0 = conn.prepareStatement(sql0);
            ps0.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveSimilarityIntoDb(long leftId,List<Map.Entry<Long, Double>> similarityList){
       // System.out.println(""+leftId+":"+ similarityList);
        //插入新数据
        for(int i=0;i<similarityList.size();i++){
            //System.out.println(""+leftId+":"+similarityList.get(i).getKey()+":"+similarityList.get(i).getValue());

            String sql = "insert into taste_item_similarity values (?,?,?)";
            try {
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setLong(1,leftId);
                ps.setLong(2,similarityList.get(i).getKey());
                ps.setDouble(3,similarityList.get(i).getValue());
                ps.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void insertLogIntoDb(int user_id,int item_id)
    {
        try{
            //查询当前item是否已存在
            boolean notInDb = true;

            String sql2 = "select * from taste_preferences where item_id = ? and user_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1,item_id);
            ps2.setInt(2,user_id);
            ResultSet rs2 = ps2.executeQuery();

            while(rs2.next()){
                notInDb = false;
            }
            //插入userid itemid
            if(notInDb){
                String sql1 = "insert into taste_preferences values(?,?)";
                PreparedStatement ps1 = conn.prepareStatement(sql1);
                ps1.setInt(1,user_id);
                ps1.setInt(2,item_id);
                ps1.execute();
                System.out.println("watch news :"+item_id+" : "+user_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getIdByUsername(String username){
        int id = -1;
        String sql0 = "select * from user where username = ?";
        try {
            PreparedStatement ps0 = conn.prepareStatement(sql0);
            ps0.setString(1, username);
            ResultSet rs = ps0.executeQuery();
            while(rs.next()){
                id = rs.getInt("id");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static  void insertLikeItemIntoDb(int item_id,int user_id){
        //查询当前item是否已存在
        boolean notInDb = true;

        String sql2 = "select * from article_like where item_id = ? and user_id = ?";
        PreparedStatement ps2 = null;
        try {
            ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1,item_id);
            ps2.setInt(2,user_id);
            ResultSet rs2 = ps2.executeQuery();

            while(rs2.next()){
                notInDb = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //插入userid itemid
        if(notInDb){
            String sql1 = "insert into article_like values(?,?)";
            PreparedStatement ps1 = null;

            try {
                ps1 = conn.prepareStatement(sql1);
                ps1.setInt(1,user_id);
                ps1.setInt(2,item_id);
                ps1.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }else{
            String sql1 = "delete from article_like where user_id = ? and item_id = ?";
            PreparedStatement ps1 = null;

            try {
                ps1 = conn.prepareStatement(sql1);
                ps1.setInt(1,user_id);
                ps1.setInt(2,item_id);
                ps1.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static  void insertUnLikeItemIntoDb(int item_id,int user_id){
        //查询当前item是否已存在
        boolean notInDb = true;

        String sql2 = "select * from article_hate where item_id = ? and user_id = ?";
        PreparedStatement ps2 = null;
        try {
            ps2 = conn.prepareStatement(sql2);
            ps2.setInt(1,item_id);
            ps2.setInt(2,user_id);
            ResultSet rs2 = ps2.executeQuery();

            while(rs2.next()){
                notInDb = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //插入userid itemid
        if(notInDb){
            String sql1 = "insert into article_hate values(?,?)";
            PreparedStatement ps1 = null;

            try {
                ps1 = conn.prepareStatement(sql1);
                ps1.setInt(1,user_id);
                ps1.setInt(2,item_id);
                ps1.execute();

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }

    public static void insertArticleTypeIntoDb(int item_id,int type){
        String sql = "insert into article_type values(?,?)";
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1,item_id);
            ps.setInt(2,type);
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
