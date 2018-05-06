package DAO;

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
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Servlet implementation class loginServlet
 */

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
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
		PrintWriter out = response.getWriter();
		response.setHeader("content-type","text/html;charset=UTF-8");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String passwd = "";
		Connection conn = (Connection) DBUtil.getConnection();
		boolean hasAccount = false;
		String sql = "select * from user where username = ?";
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				passwd = rs.getString("password");
				//System.out.println(passwd);
				hasAccount = true;
			}
			
			if(passwd.equals(password)&&hasAccount==true){
				out.write("login success");
				//request.getRequestDispatcher("/index.html").forward(request, response);
                HttpSession session = request.getSession();
                session.setAttribute("username",username);
				response.sendRedirect("articles-list.jsp");
			}else{
				out.write("login failed");
				out.write("<script type='text/javascript'>");
				out.write("var r=confirm('password error or account not exist');");
				out.write("if (r==true)");
				out.write("{");
				out.write("console.log('ok');");
				out.write(" window.location.href='login.jsp';");
				out.write("}");
				out.write("else");
				out.write("{");
				out.write("console.log('cancel');");
				out.write("}");
				out.write("</script>");
				//request.getRequestDispatcher("/login.html").forward(request, response);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
