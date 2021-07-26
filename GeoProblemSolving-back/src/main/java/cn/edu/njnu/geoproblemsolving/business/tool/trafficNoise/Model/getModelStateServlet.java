package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

@RestController
@RequestMapping(value = "/getModelState")
public class getModelStateServlet extends HttpServlet {

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();

        respJson.put("finished", "0");
        respJson.put("error", "0");

        String userId = req.getParameter("userId");
        String runningId = req.getParameter("runningId");
        String resultDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "userData" + File.separator + userId + File.separator + "OutputData" + File.separator + runningId + File.separator;

        File resultFile = new File(resultDir + "finished");
        File errorFile = new File(resultDir + "Error");

        if(errorFile.exists()){
            respJson.put("finished", "1");
            respJson.put("error", "1");
        }

        if(resultFile.exists()){
            respJson.put("finished", "1");
        }

        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
