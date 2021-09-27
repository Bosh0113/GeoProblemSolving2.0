package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;


import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.*;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.osr.CoordinateTransformation;
import org.gdal.osr.SpatialReference;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.fileUtils.upZipFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.*;

@RestController
@RequestMapping(value = "/resultInterpolation")
public class resultInterpolationServlet extends HttpServlet {

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();

        String resultUrl = req.getParameter("resultData");
        String maxLat = req.getParameter("maxLat");
        String maxLon = req.getParameter("maxLon");
        String minLat = req.getParameter("minLat");
        String minLon = req.getParameter("minLon");
        String sampleSize = req.getParameter("sampleSize");

//        结果数据的存放路径
        String resultId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + resultId + File.separator;
        String resultDir = req.getServletContext().getRealPath("./") + zipUrl;
        File resultFolder = new File(resultDir);
        if (!resultFolder.exists()) resultFolder.mkdirs();


        // 下载数据
        String result = resultDir + "NoiseResult.zip";
        try {
            URL httpURL = new URL(resultUrl);
            FileUtils.copyURLToFile(httpURL, new File(result));
        } catch (Exception ex) {
            respJson.put("respCode", "0");
            respJson.put("msg", "failed.");
            return respJson.toString();
        }

        // 解压文件
        upZipFile(new File(result).getCanonicalPath(), resultDir);

        String resultName = resultDir + "NoiseResult.shp";
        File resultShpFile = new File(resultName);

        //  等待解压结束
        long waitBegin = new Date().getTime();
        while (!(resultShpFile.exists())) {
            //  等待时间超过5秒则判定解压有问题
            if ((new Date().getTime() - waitBegin) > 5000)
                break;
        }

        copyDbfFile(resultDir + "NoiseResult");

        try {
            // 插值
            File pointsCSV = new File(resultDir + "points.csv");
            pointsCSV.createNewFile();
            FileOutputStream pointOutputStream = new FileOutputStream(pointsCSV);
            StringBuffer stringBuffer = new StringBuffer();

            DataSource dSource = getDataSource(resultName);
            if (dSource == null) {
                respJson.put("respCode", "0");
                respJson.put("msg", "failed.");
                return respJson.toString();
            }
            Layer layer = dSource.GetLayerByIndex(0);
            if (layer == null) {
                respJson.put("respCode", "0");
                respJson.put("msg", "failed.");
                return respJson.toString();
            }

            SpatialReference projectionReference = layer.GetSpatialRef();
            SpatialReference WGS84 = new SpatialReference();
            WGS84.SetWellKnownGeogCS("WGS84");
            CoordinateTransformation transformation = new CoordinateTransformation(projectionReference, WGS84);

            // 实际最大最小值
            double maxX, maxY, minX, minY;
            double y1, x1, y2, x2;
            maxX = maxY = -1;
            minX = minY = 99999999;
            y1 = x1 = y2 = x2 = 0;

            Feature feature = layer.GetNextFeature();
            while (feature != null) {
                Geometry geometry = feature.GetGeometryRef();
                double x = geometry.GetX();
                double y = geometry.GetY();
                if (x > maxX) {
                    maxX = x;
                    y1 = y;
                }
                if (y > maxY) {
                    maxY = y;
                    x1 = x;
                }
                if (x < minX) {
                    minX = x;
                    y2 = y;
                }
                if (y < minY) {
                    minY = y;
                    x2 = x;
                }

                feature = layer.GetNextFeature();
            }

            double[] latLng1 = transformation.TransformPoint(maxX, y1);
            double[] latLng2 = transformation.TransformPoint(x1, maxY);
            double[] latLng3 = transformation.TransformPoint(minX, y2);
            double[] latLng4 = transformation.TransformPoint(x2, minY);
            double[] origin1 = latLng1;
            origin1 = origin1[0] > latLng2[0] ? latLng2 : origin1;
            origin1 = origin1[0] > latLng3[0] ? latLng3 : origin1;
            origin1 = origin1[0] > latLng4[0] ? latLng4 : origin1;
            double[] origin2 = latLng1;
            origin2 = origin2[1] > latLng2[1] ? latLng2 : origin2;
            origin2 = origin2[1] > latLng3[1] ? latLng3 : origin2;
            origin2 = origin2[1] > latLng4[1] ? latLng4 : origin2;
            double[] origin3 = latLng1;
            origin3 = origin3[1] < latLng2[1] ? latLng2 : origin3;
            origin3 = origin3[1] < latLng3[1] ? latLng3 : origin3;
            origin3 = origin3[1] < latLng4[1] ? latLng4 : origin3;

            double[] targetMin = {Double.parseDouble(minLat), Double.parseDouble(minLon)};
            double[] targetMax = {Double.parseDouble(maxLat), Double.parseDouble(maxLon)};


            // 生成points.csv
            layer.ResetReading();
            feature = layer.GetNextFeature();
            double[] latLng = new double[2];
            while (feature != null) {
                Geometry geometry = feature.GetGeometryRef();
                double noiseValue = feature.GetFieldAsDouble("noise_10");
                double x = geometry.GetX();
                double y = geometry.GetY();
                latLng = transformation.TransformPoint(x, y);

                double[] newLatlng = customizedTransformation(origin1, origin2, origin3, targetMin, targetMax, latLng);
                double lat = newLatlng[0];
                double lon = newLatlng[1];

                stringBuffer.append(lon + " ," + lat + " ," + noiseValue + "\n");
                pointOutputStream.write(stringBuffer.toString().getBytes("utf-8"));
                stringBuffer.setLength(0);

                feature = layer.GetNextFeature();
            }
            double resolution = (Double.valueOf(sampleSize) / 4.0);
            resolution = resolution > 1 ? resolution : 1;
            int outsizeX = (int) ((maxX - minX) / resolution);
            int outsizeY = (int) ((maxY - minY) / resolution);

            String tempDir = ResourceUtils.getURL("classpath:").getPath() + "data" + File.separator + "temp" + File.separator;
            Path gdalGridDest = Paths.get(new File(tempDir + "gdal_grid.exe").getCanonicalPath());
//                    Path gdalGridDest = Paths.get(tempDir + "gdal_grid.exe");
            File gdalGrid = new File(resultDir + "gdal_grid.exe");
            Files.copy(gdalGridDest, Paths.get(gdalGrid.getCanonicalPath()));

            Path vrtFileDest = Paths.get(new File(tempDir + "points.vrt").getCanonicalPath());
//                    Path vrtFileDest = Paths.get(tempDir + "points.vrt");
            File vrtFile = new File(resultDir + "points.vrt");
            Files.copy(vrtFileDest, Paths.get(vrtFile.getCanonicalPath()));

//                    等待拷贝结束
            waitBegin = new Date().getTime();
            while (!gdalGrid.exists() || !vrtFile.exists()) {
                if ((new Date().getTime() - waitBegin) > 5000)
                    System.out.println("Copy Error!");
                break;
            }

//                    获得盘符
            String driver = gdalGrid.getCanonicalPath().substring(0, gdalGrid.getCanonicalPath().indexOf('\\'));
            Runtime runtime = Runtime.getRuntime();
            String cmdStr = "cmd /c cd " + resultDir + " & " + driver + " & " + gdalGrid +
                    "  -a invdist:power=3.0:smoothing=0.0:radius1=12:radius2=12 -a_srs EPSG:4326 -txe "
                    + minLon + " " + maxLon + " -tye " + maxLat + " " + minLat + " -outsize " + outsizeX + " " + outsizeY +
                    " -of GTiff -ot Float64 -l points points.vrt NoiseResult.tif";
            System.out.println(cmdStr);
            Process process = runtime.exec(cmdStr);
            process.waitFor();
            File finished = new File(resultDir + "finished");
            finished.createNewFile();

        } catch (Exception e) {
            // 直接返回错误代码JSON
            System.out.println(e.getMessage());
            respJson.put("respCode", "0");
            respJson.put("msg", "failed.");
            return respJson.toString();
        }


        respJson.put("respCode", "1");
        respJson.put("msg", "success.");
        respJson.put("resultId", resultId);
        return respJson.toString();
    }

    @RequestMapping(method = RequestMethod.GET)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req,resp);
    }

    private double[] customizedTransformation(double[] origin1, double[] origin2, double[] origin3, double[] targetMin, double[] targetMax, double[] coordinate) {
        double[] result = new double[2];

        result[0] = coordinate[0];
        result[1] = coordinate[1];

//        // 旋转
//        double angel = Math.atan((origin1[1] - origin2[1]) / (origin2[0] - origin1[0]));
//        result[0] = (result[0] - origin1[0]) * Math.cos(angel) - (result[1] - origin1[1]) * Math.sin(angel) + origin1[0];
//        result[1] = (result[1] - origin1[1]) * Math.cos(angel) + (result[0] - origin1[0]) * Math.sin(angel) + origin1[1];
//
//        // 缩放
//        double latR = (targetMax[0] - targetMin[0]) / Math.sqrt((origin2[0] - origin1[0]) * (origin2[0] - origin1[0]) + (origin2[1] - origin1[1]) * (origin2[1] - origin1[1]));
//        double lngR = (targetMax[1] - targetMin[1]) / Math.sqrt((origin3[0] - origin1[0]) * (origin3[0] - origin1[0]) + (origin3[1] - origin1[1]) * (origin3[1] - origin1[1]));
//        result[0] = (result[0] - origin1[0]) * latR + origin1[0];
//        result[1] = (result[1] - origin1[1]) * lngR + origin1[1];

        // 平移
        double offsetLat = targetMin[0] - origin1[0];
        double offsetLon = targetMin[1] - origin1[1];
        result[0] = result[0] + offsetLat;
        result[1] = result[1] + offsetLon;

        return result;
    }
}
