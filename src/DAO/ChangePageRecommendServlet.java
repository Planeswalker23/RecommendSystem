package DAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/ChangePageRecommendServlet")
public class ChangePageRecommendServlet extends HttpServlet {

    public static int index = 0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String name = request.getParameter("name");
        //System.out.println(name);
        if(name.equals("next")){
            index+=16;
        }else if(name.equals("last")){
            index-=16;
        }
       // System.out.println(index);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

}

    public static int getPage(){
        return index;
    }
}
