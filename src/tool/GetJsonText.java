package tool;

import net.sf.json.JSONObject;
import util.DBUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GetJsonText {

	public static JSONObject getArticleJSONObjectFromNet(int d) {
		//public static void main(String[] args){
		// TODO Auto-generated method stub
		JSONObject obj = null;
		String JsonContent = "";
		page ps = null;
		String path = "http://www.textvalve.com/htdatasub/subscribe/articles/v2/article-"+d;
		try {


			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();

			int code = conn.getResponseCode();
			if(code==200){
				BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				StringBuilder sb = new StringBuilder();
				String temp = "";
				while((temp=bf.readLine())!=null){
					sb.append(temp);
				}
				JsonContent = sb.toString();
				//System.out.println(JsonContent);
			}

			obj = JSONObject.fromObject(JsonContent);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	//从网络获取文章信息
	public static List<NewsList> getArticleJSONObjectFromNet(int begin, int end){
		List<NewsList> ls = new ArrayList<>();
		for (int i = begin; i < end; i++){
			JSONObject as = getArticleJSONObjectFromNet(i);
			NewsList item = new NewsList();
			item.setId(as.getJSONObject("data").getInt("id"));
			item.setTitle(as.getJSONObject("data").getString("title"));
			item.setImage_list(as.getJSONObject("data").getString("image_list"));
			ls.add(item);
		}
		return ls;
	}
	
	public static JSONObject getArticleJsonObjFromNet(int d) {
		// TODO Auto-generated method stub
		JSONObject obj = null;
		String JsonContent = "";
		page ps = null;
		String path = "http://www.textvalve.com/htdatasub/subscribe/articles/v2/article-"+d;
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			conn.connect();
			
			int code = conn.getResponseCode();
			if(code==200){
				BufferedReader bf = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
				StringBuilder sb = new StringBuilder();
				String temp = "";
				while((temp=bf.readLine())!=null){
					sb.append(temp);
				}
				JsonContent = sb.toString();
				//System.out.println(JsonContent);	
			}
			obj = JSONObject.fromObject(JsonContent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;
	}

	public static List<NewsList> getArticlesFromNet(int begin,int end){
		List<NewsList> ls = new ArrayList<>();
		for(int i = begin; i<end; i++){
			JSONObject art =  getArticleJsonObjFromNet(i);
			NewsList item = new NewsList();
			item.setId(art.getJSONObject("data").getInt("id"));
			item.setTitle(art.getJSONObject("data").getString("title"));
			item.setImage_list(art.getJSONObject("data").getString("image_list"));
			ls.add(item);
		}
		return ls;
	}

	public static JSONObject getArticleFromFile(){
		
		FileInputStream fs;
		StringBuilder sb = null;
		try {
			fs = new FileInputStream(new File(""));
			BufferedReader bf = new BufferedReader(new InputStreamReader(fs));
			String temp = "";
			sb = new StringBuilder();
			while((temp=bf.readLine())!=null){
				sb.append(temp);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		String JsonContent = sb.toString();
		return JSONObject.fromObject(JsonContent);
	}
	

	public static void insertArticleObjIntoDB(JSONObject obj){
		Connection conn1 = (Connection) DBUtil.getConnection();
		int id = obj.getJSONObject("data").getInt("id");
		String title = obj.getJSONObject("data").getString("title");
		System.out.println(id);
		String author = obj.getJSONObject("data").getString("author");
		String content = obj.getJSONObject("data").getString("content");
		String keywords = obj.getJSONObject("data").getString("keywords");
		String description = obj.getJSONObject("data").getString("description");
		int reads = obj.getJSONObject("data").getInt("reads");
		int likes = obj.getJSONObject("data").getInt("likes");
		double score = obj.getJSONObject("data").getDouble("score");
		String source_url = obj.getJSONObject("data").getString("source_url");
		String source_site = obj.getJSONObject("data").getString("source_site");
		String image_list = obj.getJSONObject("data").getString("image_list");
		boolean is_read = obj.getJSONObject("data").getBoolean("is_read");
		boolean is_like = obj.getJSONObject("data").getBoolean("is_like");
		long crawl_time =	obj.getJSONObject("data").getLong("crawl_time");
		String content_time = obj.getJSONObject("data").getString("content_time");
		String feed_time = obj.getJSONObject("data").getString("feed_time");
		String user_add_flag = obj.getJSONObject("data").getString("user_add_flag");
		
		System.out.print(id+","+title+","+author+","+content+","+keywords+","+description+","+reads+","
			+likes+","+score+","+source_url+","+is_read+","+is_like+","+crawl_time+","+content_time+","
			+user_add_flag);
		System.out.println();
				
		String sql = "insert into article values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement p = conn1.prepareStatement(sql);
			p.setInt(1, id);
			p.setString(2,title);
			p.setString(3,author);
			p.setString(4,content);
			p.setString(5,keywords);
			p.setString(6,description);
			p.setInt(7,reads);
			p.setInt(8,likes);
			p.setDouble(9,score);
			p.setString(10, source_url);
			p.setString(11, source_site);
			p.setString(12, image_list);
			p.setString(13, is_read==true?"true":"false");
			p.setString(14, is_like==true?"true":"false");
			p.setLong(15, crawl_time);
			p.setString(16, content_time);
			p.setString(17, feed_time);
			p.setString(18, user_add_flag);
			p.execute();
			conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}		
	
	public static List<NewsList> getArticlesFromDB(int begin,int end){
		List<NewsList> news = new ArrayList<NewsList>();
		Connection conn1 = (Connection) DBUtil.getConnection();
		String sql = "select * from article where id>=? and id<?";
		try {
			PreparedStatement ps = conn1.prepareStatement(sql);
			ps.setInt(1, begin);
			ps.setInt(2, end);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				NewsList item = new NewsList();
				item.setId(rs.getInt("id"));
				item.setTitle(rs.getString("title"));
				item.setImage_list(rs.getString("image_list"));
				item.setCrawl_time(rs.getLong("crawl_time"));
				news.add(item);
			}
			conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;	
	}

	public static NewsList getNewsFromDB(int id){
		NewsList item = new NewsList();
		Connection conn1 = (Connection) DBUtil.getConnection();
		String sql = "select * from article where id = ?";
		try {
			PreparedStatement ps = conn1.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				item.setId(rs.getInt("id"));
				item.setTitle(rs.getString("title"));
				item.setImage_list(rs.getString("image_list"));
				item.setContent(rs.getString("content"));
				item.setCrawl_time(rs.getLong("crawl_time"));
			}
			conn1.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	public static List<NewsList> getArticlesByTypeFromDB(int type,int howmany){
		List<NewsList> news = new ArrayList<NewsList>();
		Connection conn = (Connection) DBUtil.getConnection();
		String sql = "select * from article_type where type = ?";
		int d = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,type);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				int item_id = rs.getInt("item_id");
				NewsList ns = getNewsFromDB(item_id);
				news.add(ns);
				if(d>howmany){
					break;
				}
				d++;
			}
			System.out.println(news.size()+":"+type);
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return news;
	}

	public static String getTimeById(int id){
		NewsList ns = getNewsFromDB(id);
		long t = ns.getCrawl_time();
		Date date = new Date(t);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(date.getTime());
		return s;
	}

}
