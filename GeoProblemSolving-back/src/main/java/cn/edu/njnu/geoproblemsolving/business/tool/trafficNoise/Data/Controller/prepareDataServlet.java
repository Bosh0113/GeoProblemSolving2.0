package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao.prepareData.*;

@WebServlet(name = "prepareDataServlet", urlPatterns = "/prepareDataServlet")
public class prepareDataServlet extends HttpServlet {

    final static int THRESHOOLD_SIZE = 1024 * 1024 * 3;     //内存临界值 3MB
    final static int FILE_MAX_SIZE = 1024 * 1024 * 40;      //文件上传最大值 40MB
    final static int MAX_SIZE = 1024 * 1024 * 50;           //设置最大请求值 50MB
    final static String ROAD_FILE_NAME = "RoadCenterLine";  //上传后道路文件命名
    final static String BUILDING_FILE_NAME = "Building";    //上传后建筑物文件命名
    final static String BARRIER_FILE_NAME = "Barrier";      //上传后道路屏障文件命名

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("utf-8");

        JSONObject respJson = new JSONObject();
        PrintWriter out = resp.getWriter();

        String isDemo = req.getParameter("demo");

//        配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
//        设置内存临界值 - 超过次值后产生临时文件并储存于临时目录中
        factory.setSizeThreshold(THRESHOOLD_SIZE);
//        设置临时储存目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        ServletFileUpload upload = new ServletFileUpload(factory);
//        设置最大文件上传值
        upload.setFileSizeMax(FILE_MAX_SIZE);
//        设置最大请求值（包含文件和表单数据）
        upload.setSizeMax(MAX_SIZE);
//        中文处理
        upload.setHeaderEncoding("UTF-8");

//        生成uid
        String uid = "_" + UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String uploadPath = req.getServletContext().getRealPath("./") + "data" + File.separator + "instances" + File.separator + uid + File.separator;
//        String uploadPath = req.getServletContext().getRealPath("./") + "data" + File.separator + "userData" + File.separator + uid + File.separator;
//        存放文件上传文件夹
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

//        如果是用户自己上传的数据
        if(isDemo==null||!isDemo.equals("demo")) {
            int[] flag = new int[12];
            try {
                List<FileItem> allItems = upload.parseRequest(req);
//            数组按顺序分别为road、building、barrier的shp、prj、dbf、shx文件
//            若指定了多个同类型文件，则出错
                for (FileItem item : allItems) {
                    String itemName = item.getName();
                    if (itemName == null)
                        continue;
                    String itemFieldName = item.getFieldName();
//                获得拓展名
                    String entension = itemName.substring(itemName.lastIndexOf('.'));
//                拓展名的continue需要和name的分开，因为若name=null，获取拓展名会出错
                    if (!(entension.equals(".shp") || entension.equals(".prj") || entension.equals(".dbf") || entension.equals(".shx")))
                        continue;

                    if (itemFieldName.equals("roadFile")) {
                        String filePath = uploadPath + ROAD_FILE_NAME + entension;
                        File storeFile = new File(filePath);
                        item.setFieldName(ROAD_FILE_NAME + entension);
                        if (entension.equals(".shp")) {
                            if (flag[0] == 0) {
                                item.write(storeFile);
                                flag[0] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".prj")) {
                            if (flag[1] == 0) {
                                item.write(storeFile);
                                flag[1] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".dbf")) {
                            if (flag[2] == 0) {
                                item.write(storeFile);
                                flag[2] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else {
                            if (flag[3] == 0) {
                                item.write(storeFile);
                                flag[3] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        }
                    } else if (itemFieldName.equals("buildingFile")) {
                        String filePath = uploadPath + BUILDING_FILE_NAME + entension;
                        File storeFile = new File(filePath);
                        item.setFieldName(BUILDING_FILE_NAME + entension);
                        if (entension.equals(".shp")) {
                            if (flag[4] == 0) {
                                item.write(storeFile);
                                flag[4] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".prj")) {
                            if (flag[5] == 0) {
                                item.write(storeFile);
                                flag[5] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".dbf")) {
                            if (flag[6] == 0) {
                                item.write(storeFile);
                                flag[6] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else {
                            if (flag[7] == 0) {
                                item.write(storeFile);
                                flag[7] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        }
                    } else if (itemFieldName.equals("barrierFile")) {
                        String filePath = uploadPath + BARRIER_FILE_NAME + entension;
                        File storeFile = new File(filePath);
                        item.setFieldName(BARRIER_FILE_NAME + entension);
                        if (entension.equals(".shp")) {
                            if (flag[8] == 0) {
                                item.write(storeFile);
                                flag[8] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".prj")) {
                            if (flag[9] == 0) {
                                item.write(storeFile);
                                flag[9] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else if (entension.equals(".dbf")) {
                            if (flag[10] == 0) {
                                item.write(storeFile);
                                flag[10] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        } else {
                            if (flag[11] == 0) {
                                item.write(storeFile);
                                flag[11] = 1;
                            } else throw new Exception("0&The specified data is not valid.");
                        }
                    }
                }


            } catch (Exception e) {
                String code = e.getMessage().substring(0, 1);
                String msg = e.getMessage().substring(2);

                respJson.put("respCode", Integer.parseInt(code));
                respJson.put("msg", msg);
                out.write(respJson.toString());
                out.close();
                return;
            }
        }

//        使用测试数据
        else if(isDemo.equals("demo")) {
            File demoDir = new File(req.getServletContext().getRealPath("./") + "data" + File.separator + "demo" + File.separator);
            File[] allFiles = demoDir.listFiles();
            for (File file : allFiles) {
                if(file.isDirectory()) {
                    continue;
                }
                String fileName = file.getName();
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                if(!fileExtension.equals(".zip")&&!fileExtension.equals(".mdbf")) {
                    Path sourcePath = file.toPath();
                    Path destPath = Paths.get(uploadPath + fileName);
                    Files.copy(sourcePath, destPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }


        String dataDir = uploadPath;
        String roadFilePath = dataDir + ROAD_FILE_NAME  + ".shp";
        String buildingFilePath = dataDir + BUILDING_FILE_NAME + ".shp";
        String barrierFilePath = dataDir + BARRIER_FILE_NAME + ".shp";
        double centerLong = 0.0;
        double centerLat = 0.0;
        int count = 0;
        int buildingMaxID = 0;

        respJson.put("roadFile", 0);
        respJson.put("buildingFile", 0);
        respJson.put("barrierFile", 0);

        File roadFile = new File(roadFilePath);
        if (roadFile.exists()) {
            respJson.put("roadFile", 1);
            JSONObject resultJSON = new JSONObject();
            resultJSON = prepareRoadData(dataDir + ROAD_FILE_NAME);
//            因为玄武盾原因，复制mdbf文件要放在更改属性之后
            copyDbfFile(dataDir + ROAD_FILE_NAME);
            count++;
            if (resultJSON.size() > 0) {
                centerLong += resultJSON.getDouble("Lng");
                centerLat += resultJSON.getDouble("Lat");
                buildingMaxID = resultJSON.getInteger("maxID");
                respJson.put("roadMaxID", buildingMaxID);
            }
        }

        File buildingFile = new File(buildingFilePath);
        if (buildingFile.exists()) {
            respJson.put("buildingFile", 1);
            JSONObject resultJSON = new JSONObject();
            resultJSON = prepareBuildingData(dataDir + BUILDING_FILE_NAME);
//            因为玄武盾原因，复制mdbf文件要放在更改属性之后
            copyDbfFile(dataDir + BUILDING_FILE_NAME);
            count++;
            if (resultJSON.size() > 0) {
                centerLong += resultJSON.getDouble("Lng");
                centerLat += resultJSON.getDouble("Lat");
                buildingMaxID = resultJSON.getInteger("maxID");
                respJson.put("buildingMaxID", buildingMaxID);
            }
        }

        File barrierFile = new File(barrierFilePath);
        if (barrierFile.exists()) {
            respJson.put("barrierFile", 1);
            JSONObject resultJSON = new JSONObject();
            resultJSON = prepareBarrierData(dataDir + BARRIER_FILE_NAME);
//            因为玄武盾原因，复制mdbf文件要放在更改属性之后
            copyDbfFile(dataDir + BARRIER_FILE_NAME);
            count++;
            if (resultJSON.size() > 0) {
                centerLong += resultJSON.getDouble("Lng");
                centerLat += resultJSON.getDouble("Lat");
                buildingMaxID = resultJSON.getInteger("maxID");
                respJson.put("barrierMaxID", buildingMaxID);
            }
        }
        if (count != 0) {
            centerLong /= count;
            centerLat /= count;
        }

        respJson.put("respCode", 1);
        respJson.put("msg", "success.");
        respJson.put("dataDir", dataDir);
        respJson.put("uid", uid);
        respJson.put("centerLong", centerLong);
        respJson.put("centerLat", centerLat);

        out.write(respJson.toString());
        out.close();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }
}
