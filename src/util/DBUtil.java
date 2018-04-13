package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
	public static Connection getConnection(){
		Connection conn=null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/recommend?useUnicode=true&amp;characterEncoding=utf-8",
				"root","admin");
		} catch (ClassNotFoundException e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return conn;
		
		
	}
	
	public static void close(Connection conn){
		if(conn!=null)
		{
			try{
				conn.close();
				
			}catch (SQLException e) {
				e.printStackTrace();
				// TODO: handle exception
			}
		}
	}
}
