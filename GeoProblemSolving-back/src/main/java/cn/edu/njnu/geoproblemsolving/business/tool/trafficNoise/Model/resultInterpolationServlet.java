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

            double maxX, maxY, minX, minY;
            maxX = maxY = -1;
            minX = minY = 99999999;
            double[] latLng = new double[2];
//                    double maxLat, maxLon, minLat, minLon;
//                    maxLat = maxLon = -360;
//                    minLat = minLon = 360;
            Feature feature = layer.GetNextFeature();
            while (feature != null) {
                Geometry geometry = feature.GetGeometryRef();
                double noiseValue = feature.GetFieldAsDouble("noise_10");
                double x = geometry.GetX();
                double y = geometry.GetY();
                maxX = x > maxX ? x : maxX;
                maxY = y > maxY ? y : maxY;
                minX = x < minX ? x : minX;
                minY = y < minY ? y : minY;

                latLng = transformation.TransformPoint(x, y);
                double lon = latLng[0];
                double lat = latLng[1];
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
//                直接返回错误代码JSON
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
}
