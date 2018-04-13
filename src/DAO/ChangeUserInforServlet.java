package DAO;

import util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Servlet implementation class UserServlet
 */
@WebServlet("/ChangeUserInforServlet")
public class ChangeUserInforServlet extends HttpServlet {
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeUserInforServlet() {
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
		//PrintWriter out = response.getWriter();
		//out.println("<h1>ע��ɹ�</h1>");
		//System.out.println("ע��ɹ�");
		String username = "";
		String password = "";
		String hometown = "";
		String gender = "";
		String birthday = "";
		String email = "";
		String phonenumber = "";
		String profession = "";

		username = request.getParameter("username");
		password = request.getParameter("password");
		hometown = request.getParameter("hometown");
		gender = request.getParameter("gender");
		birthday = request.getParameter("date");
		email = request.getParameter("email");
		phonenumber = request.getParameter("phonenumber");
		profession = request.getParameter("profession");
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

		HttpSession session = request.getSession();
		String current_user = (String)session.getAttribute("username");
		System.out.println("current_user :"+current_user);

		String sql = "update user set username=?,password=?,hometown=?,gender=?,birthday=?,email=?,phone_number=?,profession=?,hobby=? where username=?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,username);
			ps.setString(2,password);
			ps.setString(3,hometown);
			ps.setString(4,gender);
			ps.setString(5,birthday);
			ps.setString(6,email);
			ps.setString(7,phonenumber);
			ps.setString(8,profession);
			ps.setString(9,hbs.toString());
			ps.setString(10,current_user);
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//request.getRequestDispatcher("/login.html").forward(request, response);
		response.sendRedirect("login.jsp");
	}

}
