package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping(value = "/getMapStatusServlet")
public class getMapStatusServlet extends HttpServlet {

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();


        HttpSession session = req.getSession(true);


        Boolean mapStatus = (Boolean)session.getAttribute("mapStatus");
        if(mapStatus != null){
//          如果有已经有输入数据，显示当前数据,包括道路、建筑物、屏障和结果数据
            String currentData = (String)session.getAttribute("currentData");
            if(currentData!=null){
                respJson.put("currentData", currentData);
            }

//          如果有保存的地图状态，则还原该状态
            Integer currentZoom = (Integer)session.getAttribute("currentZoom");
            if(currentZoom != null){
                respJson.put("currentZoom", currentZoom);
            }

            Double centerLat = (Double) session.getAttribute("centerLat");
            Double centerLong = (Double) session.getAttribute("centerLong");
            if(centerLat != null && centerLong != null){
                respJson.put("centerLat", centerLat);
                respJson.put("centerLong", centerLong);
            }

            respJson.put("respCode", 1);
            respJson.put("msg", "success.");
        }
        else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
        }

        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
