package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet(name = "saveMapStatusServlet", urlPatterns = "/saveMapStatusServlet")
public class saveMapStatusServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("utf-8");

        JSONObject respJson = new JSONObject();
        PrintWriter out = resp.getWriter();

        HttpSession session = req.getSession(true);

        if(session.getAttribute("userId")!=null){
            String currentData = req.getParameter("currentData");
            Integer currentZoom = Integer.parseInt(req.getParameter("currentZoom"));
            Double centerLat = Double.parseDouble(req.getParameter("centerLat"));
            Double centerLong = Double.parseDouble(req.getParameter("centerLong"));

//        标记是否有地图状态
            session.setAttribute("mapStatus", true);

            Date saveDate =  new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            session.setAttribute("saveDate", formatter.format(saveDate));

            session.setAttribute("currentData", currentData);
            session.setAttribute("currentZoom", currentZoom);
            session.setAttribute("centerLat", centerLat);
            session.setAttribute("centerLong", centerLong);

            respJson.put("respCode", 1);
            respJson.put("msg", "success.");
        }
        else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
        }

        out.write(respJson.toString());
        out.close();


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
