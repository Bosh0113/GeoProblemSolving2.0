package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Road.Controller;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;

import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.fileUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.prepareRoadData;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.genShpZipFile;


@RestController
@RequestMapping(value = "/exportRoadServlet")
public class exportRoadServlet extends HttpServlet {

    @Value("${dataContainer}")
    String dataContainerIp;

    final static String ROAD_FILE_NAME = "RoadCenterLine";

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(@RequestBody String data, HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();
        respJson.put("respCode", 0);

        // 解析shapefile压缩包
        try {
            JSONObject map = JSONObject.parseObject(data);
            String roadId = map.getString("uid");


            String inputDataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "TrafficNoise" + File.separator;
            String roadDir = inputDataDir + roadId + File.separator + "temp" + File.separator;
            if(!new File(roadDir).exists()) {
                genShpZipFile(roadDir, inputDataDir + roadId, ROAD_FILE_NAME);
            }
            File roadCenterLineFile = new File(roadDir + ROAD_FILE_NAME + ".zip");

            RestTemplateUtil httpUtil = new RestTemplateUtil();
            if (roadCenterLineFile.exists()) {
                LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
                String fileTemp = roadDir + ROAD_FILE_NAME + ".zip";
                FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
                valueMap.add("datafile", multipartFile);
                valueMap.add("name", ROAD_FILE_NAME);
                String uploadRemoteUrl = "http://" + dataContainerIp + ":8082/data";
                //向dataContainer传输数据
                JSONObject uploadRemoteResult = httpUtil.postRequestMap(uploadRemoteUrl, valueMap).getBody();

                Integer uploadResultInfo = uploadRemoteResult.getInteger("code");
                String dataIdInContainer = uploadRemoteResult.getJSONObject("data").getString("id");
                if (!uploadResultInfo.equals(1)) {
                    respJson.put("respCode", 0);
                    respJson.put("msg", "failed.");
                    return respJson.toJSONString();
                }
                String address = "http://" + dataContainerIp + ":8082" + "/data/" + dataIdInContainer;

                respJson.put("respCode", 1);
                respJson.put("path", address);
            } else {
                respJson.put("respCode", 0);
                respJson.put("msg", "failed.");
                return respJson.toJSONString();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
