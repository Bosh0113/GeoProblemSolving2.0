package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Building.Controller;

import com.alibaba.fastjson.JSONObject;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Layer;
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

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;

@RestController
@RequestMapping(value = "/setBuildingInfoServlet")
/*
    前台配置完BuildingConfig后传到后台，后台将前台选定参数写入shp文件
 */
public class setBuildingInfoServlet extends HttpServlet {

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(@RequestBody String data, HttpServletRequest req) throws ServletException, IOException {
        JSONObject respJson = new JSONObject();
        respJson.put("respCode", 0);
        respJson.put("msg", "failed.");

        JSONObject map = JSONObject.parseObject(data);

        JSONObject buildingPara = map.getJSONObject("buildingConfig");

        JSONObject buildingData = map.getJSONObject("buildingData");
        String uid = buildingData.getString("uid");
        String name = buildingData.getString("name");

        // 获取文件
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + uid + File.separator;
        String localDir = req.getServletContext().getRealPath("./") + zipUrl;
        String buildingFile = localDir + name + ".shp";


        DataSource dSource = getDataSource(buildingFile);
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
        int reqID = buildingPara.getInteger("id");
        Feature feature = layer.GetNextFeature();
        while (feature != null) {
            int featureID = feature.GetFieldAsInteger("OBJECTID");
            if (reqID == featureID) {
                feature.SetField("Height", buildingPara.getDouble("height"));
                feature.SetField("Storey", buildingPara.getInteger("storey"));
                layer.SetFeature(feature);

//                写入数据后要将其写入硬盘，并关闭流，否则不会成功写入
                layer.SyncToDisk();
                layer.delete();
                dSource.SyncToDisk();
                dSource.delete();
//                因为玄武盾原因，更改完属性后需要再复制一份为mdbf文件
                copyDbfFile(localDir + name);

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
