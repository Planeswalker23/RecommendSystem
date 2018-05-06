package DAO;

import tool.TurnToHtml;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/ArticleServlet")
public class ArticleServlet extends HttpServlet {
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArticleServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request,response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        response.setCharacterEncoding("UTF-8");
        response.setHeader("content-type","text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession hs = request.getSession();
        Object name = hs.getAttribute("username");
        //System.out.println(name.toString());

        Map<String,String> map = new HashMap<>();
        map.put("社会","society");
        map.put("体育","physical");
        map.put("娱乐","entertainment");
        map.put("科技","science");
        map.put("人文","cultural");
        map.put("影视","films");
        map.put("教育","education");
        map.put("游戏","game");

        ArticleServlet article = new ArticleServlet();
        String title = new String(request.getParameter("title").getBytes("ISO-8859-1"),
                "UTF-8");
        String author = name.toString();
        String type = new String(request.getParameter("type").getBytes("ISO-8859-1"),
                "UTF-8");

        type = map.get(type);

        System.out.println(type);

        String content = new String(request.getParameter("content").getBytes("ISO-8859-1"),
                "UTF-8");
        String time = Long.toString(System.currentTimeMillis());
        String keywords = "原创";

        content = content.replaceAll("\n", "<br>");
        content = content.replaceAll("\r", "<br>");

        title = "【原创】" + title;
        String str = TurnToHtml.turn(content);

        System.out.println("title：" + title);
        System.out.println("name:" + name);
        System.out.println("type:" + type);
        System.out.println("content：" + str);
        System.out.println("time:" + time);
        System.out.println("keywords:" + keywords);

        Connection conn = (Connection) DBUtil.getConnection();

        int n = 0;

       // String sql0 = "select count(*) n from articlebyuser";
//        try {
//            PreparedStatement ps0 = conn.prepareStatement(sql0);
//            ResultSet rs = ps0.executeQuery();
//            while(rs.next()){
//                n = rs.getInt("n");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        String sql = "insert into articlebyuser(title, author, type, content, keywords, time) values(?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            //ps.setInt(1, n);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, type);
            ps.setString(4, content);
            ps.setString(5, keywords);
            ps.setString(6, time);
            ps.execute();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //request.getRequestDispatcher("/login.html").forward(request, response);
        response.sendRedirect("upload.jsp");

    }

}
