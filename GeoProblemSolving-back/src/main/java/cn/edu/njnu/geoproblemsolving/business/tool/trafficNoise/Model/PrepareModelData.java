package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;


import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.*;
import com.alibaba.fastjson.JSONObject;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
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
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.*;


@RestController
@RequestMapping(value = "/prepareModelData")
public class PrepareModelData extends HttpServlet {

    @Value("${dataContainer}")
    String dataContainerIp;

    final static String ROAD_FILE_NAME = "RoadCenterLine";
    final static String BUILDING_FILE_NAME = "Building";
    final static String BARRIER_FILE_NAME = "Barrier";

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();

        String top = req.getParameter("top");
        String bottom = req.getParameter("bottom");
        String right = req.getParameter("right");
        String left = req.getParameter("left");

        String sampleSize = req.getParameter("sampleSize");
        String height = req.getParameter("height");

        String roadId = req.getParameter("roadData");
        String buildingId = req.getParameter("buildingData");
        String barrierId = req.getParameter("barrierData");


        String inputDataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "TrafficNoise" + File.separator;

        String roadDir = inputDataDir + roadId + File.separator + "temp" + File.separator;
        if(!new File(roadDir).exists()) {
            genShpZipFile(roadDir, inputDataDir + roadId, ROAD_FILE_NAME);
        }

        String buildingDir = inputDataDir + buildingId + File.separator + "temp" + File.separator;
        if(!new File(buildingDir).exists()) {
            genShpZipFile(buildingDir, inputDataDir + buildingId, BUILDING_FILE_NAME);
        }

        String barrierDir = inputDataDir + barrierId + File.separator + "temp" + File.separator;
        if(!new File(barrierDir).exists()) {
            genShpZipFile(barrierDir, inputDataDir + barrierId, BARRIER_FILE_NAME);
        }

//        输入数据的存放路径
        String resultId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + resultId + File.separator;
        String resultDir = req.getServletContext().getRealPath("./") + zipUrl;
        new File(resultDir).mkdirs();

        String boundBoxValue = left + ", " + bottom + ", " + right + ", " + top;

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

            respJson.put("road", address);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        if (buildingFile.exists()) {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            String fileTemp = buildingDir + BUILDING_FILE_NAME + ".zip";
            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
            valueMap.add("datafile", multipartFile);
            valueMap.add("name", BUILDING_FILE_NAME);
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

            respJson.put("building", address);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        if (barrierFile.exists()) {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            String fileTemp = barrierDir + BARRIER_FILE_NAME + ".zip";
            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
            valueMap.add("datafile", multipartFile);
            valueMap.add("name", BARRIER_FILE_NAME);
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

            respJson.put("barrier", address);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        if (regionBBoxUdxFile.exists()) {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            String fileTemp = resultDir + "udx_xml_RegionBBox.xml";
            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
            valueMap.add("datafile", multipartFile);
            valueMap.add("name", "udx_xml_RegionBBox");
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

            respJson.put("regionBBox", address);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        if (heightUdxFile.exists()) {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            String fileTemp = resultDir + "udx_xml_Height.xml";
            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
            valueMap.add("datafile", multipartFile);
            valueMap.add("name", "udx_xml_Height");
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

            respJson.put("height", address);
        } else {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toJSONString();
        }

        if (samplingSizeUdxFile.exists()) {
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            String fileTemp = resultDir + "udx_xml_SamplingSize.xml";
            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件
            valueMap.add("datafile", multipartFile);
            valueMap.add("name", "udx_xml_SamplingSize");
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

            respJson.put("samplingSize", address);
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
