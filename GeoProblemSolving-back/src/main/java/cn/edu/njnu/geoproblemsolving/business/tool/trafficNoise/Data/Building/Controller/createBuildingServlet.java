package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Building.Controller;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.UserData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.gdal.ogr.*;

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

@WebServlet(name = "createBuildingServlet", urlPatterns = "/createBuildingServlet")
public class createBuildingServlet extends HttpServlet {
    final static String BUILDING_FILE_NAME = "Building";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


//        resp.setCharacterEncoding("utf-8");
//        resp.setContentType("utf-8");
//        JSONObject respJson = new JSONObject();
//        PrintWriter out = resp.getWriter();
//        respJson.put("respCode", 0);
//        respJson.put("msg", "failed.");
//
//        String userId = req.getParameter("userId");
//        String buildingDataId = req.getParameter("buildingDataId");
//
//        String prop = req.getParameter("prop");
//        JSONObject buildingProp = JSONObject.parseObject(prop);
////        获取点集的JSON数组
//        JSONArray pointsArr = JSONArray.parseArray(req.getParameter("prjPoint"));
//        int OBJECTID = buildingProp.getInteger("OBJECTID");
//        double height = buildingProp.getDouble("Height");
//        int storey = buildingProp.getInteger("Storey");
//
//        String dataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "userData" + File.separator + userId + File.separator + "InputData" + File.separator + BUILDING_FILE_NAME + File.separator + buildingDataId + File.separator;
//
//
//
//        String buildingFile = dataDir + BUILDING_FILE_NAME + ".shp";
//
//        DataSource dSource = getDataSource(buildingFile);
//        if (dSource == null) {
//            out.write(respJson.toString());
//            out.close();
//            return;
//        }
//        Layer layer = dSource.GetLayerByIndex(0);
//        if (layer == null) {
//            out.write(respJson.toString());
//            out.close();
//            return;
//        }
//
//
//
//        FeatureDefn oDefn = layer.GetLayerDefn();
//        Feature tempFeature = new Feature(oDefn);
//        tempFeature.SetField("OBJECTID", OBJECTID);
//        tempFeature.SetField("Height", height);
//        tempFeature.SetField("Storey", storey);
//
//        Geometry ring = new Geometry(ogr.wkbLinearRing);
//        for(int i=0,l=pointsArr.size();i<l;i++){
//            JSONArray point = pointsArr.getJSONArray(i);
//            ring.AddPoint_2D(point.getDouble(0),point.getDouble(1));
//        }
//        ring.CloseRings();
//
//        Geometry polygon = new Geometry(ogr.wkbPolygon);
//        polygon.AddGeometry(ring);
//        tempFeature.SetGeometry(polygon);
//        layer.CreateFeature(tempFeature);
//
//        layer.SyncToDisk();
//        layer.delete();
//        dSource.SyncToDisk();
//        dSource.delete();
//
////              因为玄武盾的原因，需要拷贝一份mdbf文件
//        copyDbfFile(dataDir + BUILDING_FILE_NAME);
//
//        UserData userData = new UserData(userId);
//        userData.updateData(buildingDataId, EDataType.Building);
//
//        respJson.put("respCode", 1);
//        respJson.put("msg", "success.");
//        out.write(respJson.toString());
//        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
