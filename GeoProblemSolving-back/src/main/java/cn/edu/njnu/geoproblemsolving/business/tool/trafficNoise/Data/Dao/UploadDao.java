package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Data.Dao;

//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.EDataType;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.TrafficData;
//import cn.edu.njnu.trafficNoiseCaculating.UserData.Domain.UserData;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.Model.Service.runModelService.genShpZipFile;

public class UploadDao {
    final static int THRESHOOLD_SIZE = 1024 * 1024 * 3;     //内存临界值 3MB
    final static int FILE_MAX_SIZE = 1024 * 1024 * 40;      //文件上传最大值 40MB
    final static int MAX_SIZE = 1024 * 1024 * 50;           //设置最大请求值 50MB

    final static String _FILE_= "TrafficNoise";

    private ServletFileUpload upload;
    private List<FileItem> fileData  = new ArrayList<>();
    private Map<String, String> parameters = new HashMap<>();
//    初始化在 parseUploadData 中

    public UploadDao() {
//        配置上传参数
        DiskFileItemFactory factory = new DiskFileItemFactory();
//        设置内存临界值 - 超过次值后产生临时文件并储存于临时目录中
        factory.setSizeThreshold(THRESHOOLD_SIZE);
//        设置临时储存目录
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
        this.upload = new ServletFileUpload(factory);
//        设置最大文件上传值
        upload.setFileSizeMax(FILE_MAX_SIZE);
//        设置最大请求值（包含文件和表单数据）
        upload.setSizeMax(MAX_SIZE);
//        中文处理
        upload.setHeaderEncoding("UTF-8");
    }

    public ServletFileUpload getUpload() {
        return this.upload;
    }

    public Map<String, String> getParameters() {
        return this.parameters;
    }

    public void parseUploadData(List<FileItem> allItems) {
        try {
            for (FileItem item : allItems) {
//                如果是表单数据
                if(item.isFormField()) {
                    this.parameters.put(item.getFieldName(), item.getString("utf-8"));
                }
                else {
                    String itemName = item.getName();
                    if (itemName == null)
                        continue;
                    this.fileData.add(item);
                }
            }

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String packUploadData(String rootPath) throws IOException {
        String path = "";

        String inputDataDir = rootPath + "data" + File.separator + "InputData" + File.separator + _FILE_;

        genShpZipFile(path, inputDataDir, "tempData");
        return path;
    }

//    public boolean upload(EDataType dataType, String uploadPath, String dataId){
        public boolean upload(String dataType, String uploadPath, String dataId) {
        new File(uploadPath).mkdirs();
        for(FileItem item :this.fileData){
            try {
                String itemName = item.getName();
//              获得拓展名
                String entension = itemName.substring(itemName.lastIndexOf('.'));
                if(entension.equals(".shp") || entension.equals(".prj") || entension.equals(".dbf") ||
                        entension.equals(".shx") || entension.equals(".sbn") || entension.equals(".sbx") || entension.equals(".cpg")) {
                    String filePath = uploadPath + dataType.toString() + entension;
                    File storeFile = new File(filePath);
                    item.setFieldName(dataType.toString() + entension);
                    item.write(storeFile);
                }
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        Date now = new Date();
//        TrafficData uploadData = new (dataId, now, now);
//        this.userData.insertData(uploadData, dataType);
        return true;
    }

}
