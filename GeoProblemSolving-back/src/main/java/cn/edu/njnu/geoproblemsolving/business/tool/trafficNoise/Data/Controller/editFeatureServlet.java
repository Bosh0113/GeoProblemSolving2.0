package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

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
import java.text.DecimalFormat;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;

@WebServlet(name = "editFeatureServlet", urlPatterns = "/editFeatureServlet")
public class editFeatureServlet extends HttpServlet {
    final static String ROAD_FILE_NAME = "RoadCenterLine";
    final static String BUILDING_FILE_NAME = "Building";
    final static String BARRIER_FILE_NAME = "Barrier";

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setCharacterEncoding("utf-8");
//        resp.setContentType("utf-8");
//
//        JSONObject respJson = new JSONObject();
//        PrintWriter out = resp.getWriter();
//        respJson.put("respCode", 0);
//        respJson.put("msg", "failed.");
//
//        String userId = req.getParameter("userId");
//        String dataId = req.getParameter("dataId");
//        String editType = req.getParameter("editType");
//        String editLayer = req.getParameter("editLayer");
//        int reqID = Integer.parseInt(req.getParameter("OBJECTID"));
////        获取点集的JSON数组
//        JSONArray pointsArr = JSONArray.parseArray(req.getParameter("prjPoints"));
//
//        String inputDataDir = req.getServletContext().getRealPath("./") + "data" + File.separator + "userData" + File.separator + userId + File.separator + "InputData" + File.separator;
//
//        String dataDir = inputDataDir + editLayer + File.separator + dataId + File.separator;
//        String filePath = dataDir + editLayer + ".shp";
//
//        DataSource dSource = getDataSource(filePath);
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
//        Feature feature = layer.GetNextFeature();
//        while (feature != null) {
//            int featureID = feature.GetFieldAsInteger("OBJECTID");
//            if (reqID == featureID) {
////                删除指定要素的Geometry
//                feature.GetGeometryRef().Empty();
//                if(!editType.equals("delete")){
////                    是面元素（建筑物）
//                   if(editLayer.equals(BUILDING_FILE_NAME)){
//                       Geometry ring = new Geometry(ogr.wkbLinearRing);
//                       for(int i=0,l=pointsArr.size();i<l;i++){
//                           JSONArray point = pointsArr.getJSONArray(i);
//                           ring.AddPoint_2D(point.getDouble(0),point.getDouble(1));
//                       }
//                       Geometry polygon = new Geometry(ogr.wkbPolygon);
//                       polygon.AddGeometry(ring);
//                       feature.SetGeometry(polygon);
//                       layer.SetFeature(feature);
//                   }
////                   是线元素（道路、声屏障）
//                   else if(editLayer.equals(ROAD_FILE_NAME)||editLayer.equals(BARRIER_FILE_NAME)){
//                       Geometry polyline = new Geometry(ogr.wkbLineString);
//                       for(int i=0,l=pointsArr.size();i<l;i++){
//                           JSONArray point = pointsArr.getJSONArray(i);
//                           polyline.AddPoint_2D(point.getDouble(0),point.getDouble(1));
//                       }
//                       feature.SetGeometry(polyline);
////                       长度保留3位小数防止写入属性表超长
//                       DecimalFormat df = new DecimalFormat("#.000");
//                       double length = Double.parseDouble(df.format(polyline.Length()));
//                       feature.SetField("Length",length);
//                       layer.SetFeature(feature);
//                   }
//                }
//                else {
//                    layer.SetFeature(feature);
//                }
//
//                layer.SyncToDisk();
//                layer.delete();
//                dSource.SyncToDisk();
//                dSource.delete();
//
//                respJson.put("respCode", 1);
//                respJson.put("msg", "success.");
//                out.write(respJson.toString());
//                out.close();
//                break;
//            }
//            feature = layer.GetNextFeature();
//        }
////              因为玄武盾的原因，需要拷贝一份mdbf文件
//        copyDbfFile(dataDir + editLayer);
//        UserData userData = new UserData(userId);
//        EDataType type = null;
//        switch (editLayer){
//            case ROAD_FILE_NAME:
//                type = EDataType.RoadCenterLine;
//                break;
//            case BUILDING_FILE_NAME:
//                type = EDataType.Building;
//                break;
//            case BARRIER_FILE_NAME:
//                type = EDataType.Barrier;
//                break;
//        }
//        userData.updateData(dataId, type);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
