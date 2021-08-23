package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.TrafficData;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.UserData;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.*;

@RestController
@RequestMapping(value = "/loadDemoDataServlet")
public class loadDemoDataServlet extends HttpServlet {
    final static String ROAD_FILE_NAME = "RoadCenterLine";
    final static String BUILDING_FILE_NAME = "Building";
    final static String BARRIER_FILE_NAME = "Barrier";

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();
        String userId = req.getParameter("userId");

        String roadDataId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String buildingDataId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String barrierDataId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String inputDataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "userData" + File.separator + userId + File.separator + "InputData" + File.separator;

        File roadDir = new File(inputDataDir + ROAD_FILE_NAME + File.separator + roadDataId);
        File buildingDir = new File(inputDataDir + BUILDING_FILE_NAME + File.separator + buildingDataId);
        File barrierDir = new File(inputDataDir + BARRIER_FILE_NAME + File.separator + barrierDataId);
        roadDir.mkdirs();
        buildingDir.mkdirs();
        barrierDir.mkdirs();

        File demoDir = new File(req.getServletContext().getRealPath("./") + "data" + File.separator + "demo" + File.separator);
        File[] allFiles = demoDir.listFiles();
        for (File file : allFiles) {
            if(file.isDirectory()) {
                continue;
            }
            String fileName = file.getName().split("\\.")[0];
            String fileExtension = file.getName().split("\\.")[1];
            String dataDir = "";
            String dataId = File.separator;
            switch (fileName){
                case ROAD_FILE_NAME:
                    dataDir = roadDir.getCanonicalPath();
                    break;
                case BUILDING_FILE_NAME:
                    dataDir = buildingDir.getCanonicalPath();
                    break;
                case BARRIER_FILE_NAME:
                    dataDir = barrierDir.getCanonicalPath();
                    break;
            }
            if(!fileExtension.equals("zip")) {
                Path sourcePath = file.toPath();
                Path destPath = Paths.get(dataDir + File.separator + file.getName());
                Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
            }
        }
//        写入dataInfo.xml
//        UserData userdata = new UserData(userId);
//        userdata.insertData(new TrafficData(roadDataId), EDataType.RoadCenterLine);
//        userdata.insertData(new TrafficData(buildingDataId), EDataType.Building);
//        userdata.insertData(new TrafficData(barrierDataId), EDataType.Barrier);

        JSONObject dataInfo;
        double centerLong = 0.0;
        double centerLat = 0.0;
//        Road
        dataInfo = prepareRoadData(inputDataDir + "RoadCenterLine" + File.separator + roadDataId + File.separator + ROAD_FILE_NAME);
        centerLong += dataInfo.getDouble("Lng");
        centerLat += dataInfo.getDouble("Lat");
        respJson.put("roadMaxID", dataInfo.getDouble("maxID"));

//        Building
        dataInfo = prepareBuildingData(inputDataDir + "Building" + File.separator + buildingDataId + File.separator + BUILDING_FILE_NAME);
        centerLong += dataInfo.getDouble("Lng");
        centerLat += dataInfo.getDouble("Lat");
        respJson.put("buildingMaxID", dataInfo.getDouble("maxID"));

//        Barrier
        dataInfo = prepareBarrierData(inputDataDir + "Barrier" + File.separator + barrierDataId + File.separator + BARRIER_FILE_NAME);
        centerLong += dataInfo.getDouble("Lng");
        centerLat += dataInfo.getDouble("Lat");
        respJson.put("barrierMaxID", dataInfo.getDouble("maxID"));
        respJson.put("roadDataId", roadDataId);
        respJson.put("buildingDataId", buildingDataId);
        respJson.put("barrierDataId", barrierDataId);
        respJson.put("centerLong", centerLong/3);
        respJson.put("centerLat", centerLat/3);
        respJson.put("respCode", 1);

        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
