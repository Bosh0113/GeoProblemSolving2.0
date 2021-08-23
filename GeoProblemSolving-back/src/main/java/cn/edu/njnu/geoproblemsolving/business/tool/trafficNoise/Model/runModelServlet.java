package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model;


import cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.modelservice.modelservice.*;
import com.alibaba.fastjson.JSONObject;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.fileUtils.*;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.copyDbfFile;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.shapefileUtil.getDataSource;
import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.*;

@RestController
@RequestMapping(value = "/runModelServlet")
public class runModelServlet extends HttpServlet {

    final static String ROAD_FILE_NAME = "RoadCenterLine";
    final static String BUILDING_FILE_NAME = "Building";
    final static String BARRIER_FILE_NAME = "Barrier";

    @RequestMapping(method = RequestMethod.POST)
    protected String doPost(HttpServletRequest req) throws ServletException, IOException {

        JSONObject respJson = new JSONObject();

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

        String roadId = req.getParameter("roadData");
        String buildingId = req.getParameter("buildingData");
        String barrierId = req.getParameter("barrierData");

//        输入数据的存放路径
        String resultId = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String zipUrl = "data" + File.separator + "TrafficNoise" + File.separator + resultId + File.separator;
        String resultDir = req.getServletContext().getRealPath("./") + zipUrl;
        new File(resultDir).mkdirs();

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

        String boundBoxValue = left + ", " + bottom + ", " + right + ", " + top;
        String resultName = "";


        genHeightUdxData(resultDir, height);
        genSamplingSizeUdxData(resultDir, sampleSize);
        genBoundBoxUdxData(resultDir, boundBoxValue);

        try {
            Server server = ServerFactory.createServer("172.21.212.2", 8060);
            if (server.connect() == 1) {
//                System.out.println("连接到模型容器！");
                ServiceAccess pServiceAccess = server.getServiceAccess();
                List<ModelService> list_ms = pServiceAccess.getModelServicesList();

                String serviceOID = "";
                for (ModelService ms : list_ms) {
                    if (ms == null) continue;
                    if (ms.getServiceName().equals("WebRLS90Parallel")) {
                        serviceOID = ms.getServiceOID();
                        break;
                    }
                }
                if (serviceOID.equals("")) {
                    System.out.println("There is no service called WebRLS90Parallel!");
//                    返回错误代码
                    respJson.put("respCode", "0");
                    respJson.put("msg", "failed.");
                    return respJson.toString();
                }

                //判断文件是否存在并上传数据
                File roadCenterLineFile = new File(roadDir + ROAD_FILE_NAME + ".zip");
                File buildingFile = new File(buildingDir + BUILDING_FILE_NAME + ".zip");
                File barrierFile = new File(barrierDir + BARRIER_FILE_NAME + ".zip");
                File heightUdxFile = new File(resultDir + "udx_xml_Height.xml");
                File regionBBoxUdxFile = new File(resultDir + "udx_xml_RegionBBox.xml");
                File samplingSizeUdxFile = new File(resultDir + "udx_xml_SamplingSize.xml");
                DataConfiguration pDataconfig = pServiceAccess.createDataConfig();

                if (roadCenterLineFile.exists()) {
                    Data roadData = pServiceAccess.uploadDataByFile(roadCenterLineFile.getCanonicalPath(), ROAD_FILE_NAME + ".zip");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputRoadCenterLineData", roadData.getID(), true);
                }
                if (buildingFile.exists()) {
                    Data buildingData = pServiceAccess.uploadDataByFile(buildingFile.getCanonicalPath(), BUILDING_FILE_NAME + "Building.zip");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputBuildingData", buildingData.getID(), true);
                }
                if (barrierFile.exists()) {
                    Data barrierData = pServiceAccess.uploadDataByFile(barrierFile.getCanonicalPath(), BARRIER_FILE_NAME + ".zip");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputBarrierData", barrierData.getID(), true);
                }
                if (regionBBoxUdxFile.exists()) {
                    Data regionBBoxData = pServiceAccess.uploadDataByFile(regionBBoxUdxFile.getCanonicalPath(), "udx_xml_RegionBBox.xml");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputRegionBBox", regionBBoxData.getID(), true);
                }
                if (heightUdxFile.exists()) {
                    Data heightData = pServiceAccess.uploadDataByFile(heightUdxFile.getCanonicalPath(), "udx_xml_Height.xml");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputHeightData", heightData.getID(), true);
                }
                if (samplingSizeUdxFile.exists()) {
                    Data samplingSizeData = pServiceAccess.uploadDataByFile(samplingSizeUdxFile.getCanonicalPath(), "udx_xml_SamplingSize.xml");
                    pDataconfig.insertData("2dfc013b-5b40-4de5-a24c-a69edf2a3772", "InputSamplingSizeData", samplingSizeData.getID(), true);
                }
                System.out.println("上传数据成功！");

//                数据上传成功后返回
                if (resultId.equals("")) {
                    respJson.put("respCode", "0");
                    respJson.put("msg", "failed.");
                    return respJson.toString();
                }

                ModelService pMservice = pServiceAccess.getModelServiceByOID(serviceOID);
                String recordid = pMservice.invoke(pDataconfig);
                ModelServiceRecord pRecord = pServiceAccess.getModelServiceRecordByID(recordid);
                int msrstatus = pRecord.getStatus().getCode();
                System.out.println(recordid);

//                System.out.println("模型正在运行！");
                while (msrstatus == 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pRecord.refresh();
                    msrstatus = pRecord.getStatus().getCode();

                }
//                System.out.println("模型运行结束！");

                File resultFile;

                //测试保存文件
                DataConfigItem item = pRecord.getOutputData().getItem().get(0);
                String dataId = item.data;
                Data outputData = pServiceAccess.getDataServiceByID(dataId);
//                有运行结果
                if (outputData != null) {
                    String dataValue = outputData.getValue();
                    //根据value值获取文件后缀
                    String ext = dataValue.substring(dataValue.lastIndexOf(".") + 1);

                    resultFile = new File(resultDir + "Result." + ext);
                    outputData.saveAs(resultFile.getCanonicalPath());

//                等待结果文件保存
                    long waitBegin = new Date().getTime();
                    while (!resultFile.exists()) {
//                    等待时间超过5秒则判定生成的结果文件有问题
                        if ((new Date().getTime() - waitBegin) > 5000)
                            break;
                    }

                    resultName = resultDir + "NoiseResult.shp";
//                解压文件
                    upZipFile(resultFile.getCanonicalPath(), resultDir);
//                等待解压结束
                    waitBegin = new Date().getTime();
                    File resultShpFile = new File(resultName);
                    while (!(resultShpFile.exists())) {
//                    等待时间超过5秒则判定解压有问题
                        if ((new Date().getTime() - waitBegin) > 5000)
                            break;
                    }
//                System.out.println("保存结果文件成功！");
                    copyDbfFile(resultDir + "NoiseResult");
                    System.out.println("Run success!");
//                    插值
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
                    Path gdalGridDest =  Paths.get(new File(tempDir + "gdal_grid.exe").getCanonicalPath());
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

                }
//                运行结果为空
                else {
//                    保存错误文档
                    File errorFile = new File(resultDir + "Error");
                    if (!errorFile.exists()) {
                        errorFile.createNewFile();
                    }

                }
            } //if(server.connect() == 1)
//            if(server.connect() != 1)--模型容器没有连接
            else {
//                直接返回错误代码JSON
                respJson.put("respCode", "0");
                respJson.put("msg", "failed.");
                return respJson.toString();
            }

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
