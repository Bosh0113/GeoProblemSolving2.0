package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Road.Controller;

import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import com.alibaba.fastjson.JSONObject;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
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
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.*;


@RestController
@RequestMapping(value = "/setRoadInfoServlet")
/*
    前台配置完RoadConfig后传到后台，后台将前台选定参数写入shp文件
 */
public class setRoadInfoServlet extends HttpServlet {
    final static String ROAD_FILE_NAME = "RoadCenterLine";

    @Value("${dataContainer}")
    String dataContainerIp;

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(@RequestBody String data, HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();
        respJson.put("respCode", 0);
        respJson.put("msg", "failed.");

        JSONObject map = JSONObject.parseObject(data);

        JSONObject roadPara = map.getJSONObject("roadConfig");

        JSONObject roadData = map.getJSONObject("roadData");
        String uid = roadData.getString("uid");
//        String name = roadData.getString("name");

        // 获取文件
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + uid + File.separator;
        String localDir = req.getServletContext().getRealPath("./") + zipUrl;
        String roadFile = localDir + ROAD_FILE_NAME + ".shp";


        DataSource dSource = getDataSource(roadFile);
        if (dSource == null) {
            return respJson.toString();
        }
        Layer layer = dSource.GetLayerByIndex(0);
        if (layer == null) {
            respJson.put("respCode", 0);
            respJson.put("msg", "failed.");
            return respJson.toString();
        }

        //将前台传过来的道路参数写入shp文件属性表
        int reqID = roadPara.getInteger("id");
        Feature feature = layer.GetNextFeature();
        while (feature != null) {
            int featureID = feature.GetFieldAsInteger("OBJECTID");
            if (reqID == featureID) {
                feature.SetField("Width", roadPara.getDouble("width"));
                feature.SetField("M", roadPara.getDouble("M"));
                feature.SetField("P", roadPara.getDouble("P"));
                feature.SetField("CarSpeed", roadPara.getDouble("carSpeed"));
                feature.SetField("TruckSpeed", roadPara.getDouble("truckSpeed"));
                int hasReflect = roadPara.getInteger("hasReflect");
                feature.SetField("HasReflect", hasReflect);
                if(hasReflect==0) {
                    feature.SetField("RefHeight", 0);
                    feature.SetField("RefWidth", 0);
                    feature.SetField("RefAbsorb", -1);
                }
                else {
                    feature.SetField("RefHeight", roadPara.getDouble("reflectHeight"));
                    feature.SetField("RefWidth", roadPara.getDouble("reflectWidth"));
                    feature.SetField("RefAbsorb", roadPara.getInteger("reflectAbsorb"));
                }
                feature.SetField("Slope", roadPara.getDouble("slope"));
                feature.SetField("Surface", roadPara.getInteger("roadSurface"));
                layer.SetFeature(feature);

//                写入数据后要将其写入硬盘，并关闭流，否则不会成功写入
                layer.SyncToDisk();
                layer.delete();
                dSource.SyncToDisk();
                dSource.delete();
//                因为玄武盾原因，更改完属性后需要再复制一份为mdbf文件
                copyDbfFile(localDir + ROAD_FILE_NAME);

                String temDir = localDir + "temp" + File.separator;
                genShpZipFile(temDir, localDir, ROAD_FILE_NAME);

                respJson.put("newAddress", File.separator + zipUrl + "temp" + File.separator + ROAD_FILE_NAME + ".zip");
                respJson.put("respCode", 1);
                respJson.put("msg", "success.");
                break;
            }
            feature = layer.GetNextFeature();
        }
        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
