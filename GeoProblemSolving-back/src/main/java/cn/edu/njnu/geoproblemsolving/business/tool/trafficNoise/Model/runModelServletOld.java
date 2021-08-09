package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.*;

@WebServlet(name = "runModelServletOld", urlPatterns = "/runModelServletOld")
public class runModelServletOld extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("utf-8");

        String dataDir = req.getParameter("dataDir");
        String uid = req.getParameter("uid");
        String maxLat = req.getParameter("maxLat");
        String maxLon = req.getParameter("maxLon");
        String minLat = req.getParameter("minLat");
        String minLon = req.getParameter("minLon");
        String sampleSize = req.getParameter("sampleSize");
        String height = req.getParameter("height");


        String resultPath = "";
        String resultName = "";

        try {
            Runtime runtime = Runtime.getRuntime();

//            运行模型
            String appDir = req.getServletContext().getRealPath("./") + "bin";
//            获得盘符
            String driver = appDir.substring(0, appDir.indexOf('\\'));
            Process process = runtime.exec("cmd /c cd " + appDir + "& " + driver + " & TrafficNoise.exe " +
                    dataDir + " " + uid + " " + minLon + " " + minLat + " " + maxLon + " " + maxLat + " " + sampleSize + " " + height);

            //java的获取屏幕输出是Input。。。
//            System.out.println("Input:");
            InputStream in = process.getInputStream();
            InputStreamReader inReader = new InputStreamReader(in);
            BufferedReader inBr = new BufferedReader(inReader);
            String strIn;
//            必须通过while来缓冲模型的运行时间，当strIn为null时，说明模型运行结束
            while ((strIn = inBr.readLine()) != null) {
                System.out.println(strIn);
            }

//            将模型的结果拷贝到data文件夹
//            读取运行结果的文件名
            String resultDir = this.getServletContext().getRealPath("") + "data" + File.separator + "result" + File.separator;
            File resultPathFile = new File(resultDir + "resultPath.txt");
            FileReader fileReader = new FileReader(resultPathFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            resultPath = bufferedReader.readLine();
            bufferedReader.close();
            if (resultPath != null) {
                resultName = resultPath.substring(resultPath.lastIndexOf('\\') + 1, resultPath.lastIndexOf('.'));
//                玄武盾会拦截对dbf的请求，将其复制为mdbf
                copyDbfFile(resultDir + resultName);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        JSONObject respJson = new JSONObject();

        if (resultName != "") {
            respJson.put("respCode", 1);
            respJson.put("msg", "success.");
            respJson.put("resultName", resultName);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
        }
        PrintWriter out = resp.getWriter();
        out.write(respJson.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req,resp);
    }
}
