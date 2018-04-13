package DAO;

import recommend.DB_io;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ReadTimeServlet")
public class ReadTimeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_id0 = request.getParameter("user_id");
        String item_id0 = request.getParameter("item_id");
        String flag = request.getParameter("flag");
        int user_id = Integer.parseInt(user_id0);
        int item_id = Integer.parseInt(item_id0);
        DB_io.insertReadTimeIntoDB(user_id,item_id,flag);
        DB_io.insertLogIntoDb(user_id,item_id);
        System.out.println("ReadTimeServlet :"+item_id+":"+user_id+":"+flag);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
