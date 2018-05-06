package DAO;

import Antispam.AntispamScanResult;
import Antispam.TextAntispamScanSample;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/FilterServlet")
public class FilterServlet extends HttpServlet {



    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String content = request.getParameter("content");
        System.out.println("content : "+content);
        AntispamScanResult asc = TextAntispamScanSample.TextFilter(content);
        PrintWriter out = response.getWriter();
        out.println(asc.getSuggestion()+":"+asc.getLabel());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

}

}
