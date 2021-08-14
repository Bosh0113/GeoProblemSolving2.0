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

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;

@RestController
@RequestMapping(value = "/getBuildingInfoServlet")
public class getBuildingInfoServlet extends HttpServlet {
    final static String BUILDING_FILE_NAME = "Building";

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(@RequestBody String data, HttpServletRequest req) throws ServletException, IOException {
        JSONObject respJson = new JSONObject();
        respJson.put("respCode", 0);
        respJson.put("msg", "failed.");

        JSONObject map = JSONObject.parseObject(data);
        String reqID = map.getString("id");

        JSONObject buildingData = (JSONObject) map.get("buildingData");
        String uid = buildingData.getString("uid");
//        String name = buildingData.getString("name");

        // 获取文件
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + uid + File.separator;
        String localDir = req.getServletContext().getRealPath("./") + zipUrl;
        String roadFile = localDir + BUILDING_FILE_NAME + ".shp";

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

        Feature feature = layer.GetNextFeature();
        while (feature != null) {
            String featureID = feature.GetFieldAsString("OBJECTID");
            if (reqID.equals(featureID)) {
                respJson.put("Height", feature.GetFieldAsDouble("Height"));
                respJson.put("Storey", feature.GetFieldAsInteger("Storey"));

                layer.delete();
                dSource.delete();

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
