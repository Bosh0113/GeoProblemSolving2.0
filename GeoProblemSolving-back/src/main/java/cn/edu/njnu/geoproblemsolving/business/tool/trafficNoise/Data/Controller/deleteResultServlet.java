package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "deleteResultServlet", urlPatterns = "/deleteResultServlet")
public class deleteResultServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("utf-8");
        String fileName = req.getParameter("0");
//        String[] file = (String[]) JSONArray.toArray(fileName, String.class);
//        JSONObject obj = new JSONObject().parseObject(fileName);
        System.out.println(fileName);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
