package DAO;

import entity.User;
import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
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
		//out.println("<h1>ע��ɹ�</h1>");
		//System.out.println("ע��ɹ�");
		
		User user = new User();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String hometown = request.getParameter("hometown");
		String gender = request.getParameter("gender");
		String birthday = request.getParameter("date");
		String email = request.getParameter("email");
		String phonenumber = request.getParameter("phonenumber");
		String profession = request.getParameter("profession");
		String[] hobbys = request.getParameterValues("hobby");
		StringBuilder hbs = new StringBuilder();
		//System.out.println(username+" "+password+" "+hometown
				//+" "+gender+" "+birthday+" "+gender+" "+
				//email+" "+phonenumber+" "+profession);
		for(int i=0;i<hobbys.length;i++){
			//System.out.println(hobbys[i]+" ");
			hbs.append(hobbys[i]+",");
		}
		
		Connection conn = (Connection) DBUtil.getConnection();

		int n = 0;

		String sql0 = "select count(*) n from user";
		try {
			PreparedStatement ps0 = conn.prepareStatement(sql0);
			ResultSet rs = ps0.executeQuery();
			while(rs.next()){
				n = rs.getInt("n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,n);
			ps.setString(2,username);
			ps.setString(3,password);
			ps.setString(4,hometown);
			ps.setString(5,gender);
			ps.setString(6,birthday);
			ps.setString(7,email);
			ps.setString(8,phonenumber);
			ps.setString(9,profession);
			ps.setString(10,hbs.toString());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//request.getRequestDispatcher("/login.html").forward(request, response);
		response.sendRedirect("login.jsp");
	}

}
