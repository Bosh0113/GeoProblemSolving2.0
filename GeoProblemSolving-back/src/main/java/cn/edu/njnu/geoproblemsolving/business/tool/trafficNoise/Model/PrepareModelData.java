package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.OutputData;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.UserData;

import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.*;
import com.alibaba.fastjson.JSONObject;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.*;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.zipUtil.unZipFile;


@RestController
@RequestMapping(value = "/prepareModelData")
public class PrepareModelData extends HttpServlet {

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();

        String userId = req.getParameter("userId");
//        前端将已经将经纬度转为投影坐标
        String maxLat = req.getParameter("maxLat");
        String maxLon = req.getParameter("maxLon");
        String minLat = req.getParameter("minLat");
        String minLon = req.getParameter("minLon");

        String top = req.getParameter("top");
        String bottom = req.getParameter("bottom");
        String right = req.getParameter("right");
        String left = req.getParameter("left");

        String sampleSize = req.getParameter("sampleSize");
        String height = req.getParameter("height");

        JSONObject roadData = JSONObject.parseObject(req.getParameter("roadData"));
        JSONObject buildingData = JSONObject.parseObject(req.getParameter("buildingData"));
        JSONObject barrierData = JSONObject.parseObject(req.getParameter("barrierData"));

//        输入数据的存放路径
        String resultId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + resultId + File.separator;
        String resultDir = req.getServletContext().getRealPath("./") + zipUrl;
        new File(resultDir).mkdirs();

        String boundBoxValue = left + ", " + bottom + ", " + right + ", " + top;
        String resultName = "";

        String inputDataDir = req.getServletContext().getRealPath("./");
        String roadDir = inputDataDir + "data" + File.separator + "TrafficNoise" + File.separator + roadData.getString("uid") + File.separator;
        String buildingDir = inputDataDir + "data" + File.separator + "TrafficNoise" + File.separator + buildingData.getString("uid") + File.separator;
        String barrierDir = inputDataDir + "data" + File.separator + "TrafficNoise" + File.separator + barrierData.getString("uid") + File.separator;

        String ROAD_FILE_NAME = roadData.getString("name");
        String BUILDING_FILE_NAME = buildingData.getString("name");
        String BARRIER_FILE_NAME = barrierData.getString("name");

//        genShpZipFile(resultDir, roadDir, ROAD_FILE_NAME);
//        genShpZipFile(resultDir, buildingDir, BUILDING_FILE_NAME);
//        genShpZipFile(resultDir, barrierDir, BARRIER_FILE_NAME);
        genHeightUdxData(resultDir, height);
        genSamplingSizeUdxData(resultDir, sampleSize);
        genBoundBoxUdxData(resultDir, boundBoxValue);

        //判断文件是否存在并上传数据
        File roadCenterLineFile = new File(roadDir + ROAD_FILE_NAME + ".zip");
        File buildingFile = new File(buildingDir + BUILDING_FILE_NAME + ".zip");
        File barrierFile = new File(barrierDir + BARRIER_FILE_NAME + ".zip");
        File heightUdxFile = new File(resultDir + "udx_xml_Height.xml");
        File regionBBoxUdxFile = new File(resultDir + "udx_xml_RegionBBox.xml");
        File samplingSizeUdxFile = new File(resultDir + "udx_xml_SamplingSize.xml");

        if (roadCenterLineFile.exists()) {
            respJson.put("roadDate", roadDir + ROAD_FILE_NAME + ".zip");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }
        if (buildingFile.exists()) {
            respJson.put("buildingDate", buildingDir + BUILDING_FILE_NAME + ".zip");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }
        if (barrierFile.exists()) {
            respJson.put("barrierDate", barrierDir + BARRIER_FILE_NAME + ".zip");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }
        if (regionBBoxUdxFile.exists()) {
            respJson.put("regionBBox", resultDir + "udx_xml_RegionBBox.xml");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }
        if (heightUdxFile.exists()) {
            respJson.put("height", resultDir + "udx_xml_Height.xml");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }
        if (samplingSizeUdxFile.exists()) {
            respJson.put("samplingSize", resultDir + "udx_xml_SamplingSize.xml");
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        respJson.put("respCode", 1);
        respJson.put("msg", "success.");
        return respJson.toJSONString();

    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req,resp);
    }
}
