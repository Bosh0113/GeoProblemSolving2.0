package cn.edu.njnu.geoproblemsolving.business.tool.trafficNoise.service;

import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

//欣畅-交通噪声工具，但是资源已经完全更改了
@Service
public class DataService {
    @Value("${dataContainer}")
    String dataRemoteIp;

    @Value("${resServerIp}")
    String userResServer;

    // @Autowired
    // IResourceDaoImpl resourceDao;

    private final String BUILDINGS = "Buildings.zip";
    private final String ROADS = "Roads.zip";
    private final String BARRIERS = "Barriers.zip";
    private final String ROI_BOUNDS = "udx_xml_RegionBox.xml";
    private final String ROI_HEIGHT = "udx_xml_Height.xml";
    private final String SAMPLING_SIZE = "udx_xml_SamplingSize.xml";

//    private final String resourcePath = ClassUtils.getDefaultClassLoader().getResource("").getPath().substring(1) + "static/";

    /**
     * @function: 噪声后台传过来数据，临时存储，成功则返回uid
     * @buildingsData:建筑物数据zip
     * @roadsData:道路数据zip
     * @barriersData:声屏障数据zip
     * @userId:用户id
     * @return: java.lang.Object
     * @date: 2021/01/14 09:37
     */
    public Object saveTempRLS90Data(HttpServletRequest req, String userId, String tempId) {
        String resourcePath = req.getSession().getServletContext().getRealPath("/");
        String uploadDir = resourcePath + "traffic_noise_tool/upload/data/" + userId + "/map_" + tempId + "/";
        (new File(uploadDir)).mkdirs();

        try {
            Collection<Part> parts = req.getParts();
            for (Part part : parts) {
                if (part.getName().equals("trafficNoise")) {
                    String tempFilePath = uploadDir + part.getSubmittedFileName();
                    File tempFile = new File(tempFilePath);
                    tempFile.createNewFile();
                    FileUtils.copyInputStreamToFile(part.getInputStream(), tempFile);
                    break;
                }
            }
        } catch (IOException | ServletException e) {
            e.printStackTrace();
            return ResultUtils.error(-2, "Upload data failed!");
        }
        return ResultUtils.success();
    }

    // public JSONObject gsmRes2UserBaseRes(IResourceEntity resourceEntity, String resMd5){
    //     JSONObject userBaseRes = new JSONObject();
    //     userBaseRes.put("uid", resourceEntity.getResourceId());
    //     userBaseRes.put("name", resourceEntity.getName());
    //     userBaseRes.put("type", resourceEntity.getType());
    //     userBaseRes.put("thumbnail",resourceEntity.getThumbnail());
    //     userBaseRes.put("editToolInfo", resourceEntity.getEditToolInfo());
    //     userBaseRes.put("fileSize", resourceEntity.getFileSize());
    //     userBaseRes.put("address", resourceEntity.getPathURL());
    //     userBaseRes.put("parent", "no info");
    //     userBaseRes.put("md5", resMd5);
    //     userBaseRes.put("privacy", resourceEntity.getPrivacy());
    //     userBaseRes.put("suffix", "");
    //     userBaseRes.put("description", resourceEntity.getDescription());
    //     userBaseRes.put("template", "");
    //     userBaseRes.put("uploadTime", resourceEntity.getUploadTime());
    //     userBaseRes.put("children", null);
    //     return userBaseRes;
    // }

//     public Object uploadRLS90Data(String userId, String tempId, HttpServletRequest req) {
//
//         String resourcePath = req.getSession().getServletContext().getRealPath("/");
//         String dataDir = resourcePath + "traffic_noise_tool/upload/data/" + userId + "/map_" + tempId + "/";
// //        if (!(new File(dataDir)).exists())
// //            return ResultUtils.error(-2, "Upload data failed!");
//
//         Map<String, String> dataMap = new HashMap<String, String>() {
//             {
//                 put("Buildings", BUILDINGS);
//                 put("Roads", ROADS);
//                 put("Barriers", BARRIERS);
//                 put("roiHeight", ROI_HEIGHT);
//                 put("samplingSize", SAMPLING_SIZE);
//                 put("roiBounds", ROI_BOUNDS);
//             }
//         };
//
//         RestTemplateUtil httpUtil = new RestTemplateUtil();
//         HttpSession session = req.getSession();
//         try {
//             for (Map.Entry entry : dataMap.entrySet()) {
//                 String filePath = dataDir + entry.getValue().toString();
//                 File dataFile = new File(filePath);
//                 if(dataFile.exists()){
// //                    IUploadResult uploadInfos = new IUploadResult();
// //                    //记录上传状态
// //                    uploadInfos.failed = new ArrayList<>();
// //                    uploadInfos.sizeOver = new ArrayList<>();
// //                    uploadInfos.uploaded = new ArrayList<>();
//                     long fileSize = dataFile.length();
//                     if (fileSize < 1024 * 1024 * 1024) {
//                         LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
//                         String fileFullName = dataFile.getName();
//                         String[] temp = fileFullName.split("\\.");
//                         String fileName = temp[0];
//                         String suffix = "." + temp[1];
//
//                         File fileTemp = File.createTempFile(fileName, suffix);//创建临时文件
//                         FileUtils.copyInputStreamToFile(new FileInputStream(dataFile), fileTemp);
//                         FileSystemResource multipartFile = new FileSystemResource(fileTemp);
//
//                         valueMap.add("datafile", multipartFile);
//                         valueMap.add("name", fileName);
//                         valueMap.add("userId", userId);
// //                        valueMap.add("userId", session.getAttribute("userId"));
//                         valueMap.add("serverNode", "china");
//                         valueMap.add("origination", "GeoProblemSolving");
//                         String uploadRemoteUrl = "http://" + dataRemoteIp + ":8082/data";
//                         //向dataContainer传输数据
//                         JSONObject uploadRemoteResult = httpUtil.uploadRemote(uploadRemoteUrl, valueMap);
//                         Integer uploadResultInfo = uploadRemoteResult.getInteger("code");
//
//                         if (!uploadResultInfo.equals(1)) {
// //                            uploadInfos.failed.add(fileName);
//                             valueMap.clear();
//                             continue;
//                         }
//
//                         //成功将资源上传到数据容器中
//                         // 接下来将资源基本信息写入本地数据库及用户服务器
//                         String fileSizeFormat;
//                         DecimalFormat df = new DecimalFormat("##0.00");
//                         if (fileSize > 1024 * 1024) {
//                             fileSizeFormat = df.format((float) fileSize / (float) (1024 * 1024)) + "MB";
//                         } else {
//                             fileSizeFormat = df.format((float) fileSize / (float) (1024)) + "KB";
//                         }
//                         Date uploadDate = new Date();
//                         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                         String uploadTime = simpleDateFormat.format(uploadDate);
//
// //                        TODO:注释部分
//                         String uploaderId = (String) session.getAttribute("userId");
//                         String uploaderName = (String) session.getAttribute("name");
//
// //                        String uploaderId = (String) userId;
// //                        String uploaderName = (String) session.getAttribute("name");
// //                         IResourceEntity resourceEntity = new IResourceEntity();
// //                         resourceEntity.setName(fileFullName);
// //                         resourceEntity.setFileSize(fileSizeFormat);
// //                         resourceEntity.setUploaderId(uploaderId);
// //                         resourceEntity.setUploaderName(uploaderName);
// //                         resourceEntity.setUploadTime(uploadTime);
// //                         resourceEntity.setPrivacy(req.getParameter("public"));
// //                         resourceEntity.setType("data");
// //
// //                         String url = "http://" + dataRemoteIp + ":8082/data/" + uploadRemoteResult.getJSONObject("data").getString("id");
// //                         resourceEntity.setPathURL(url);
// //
// //                         resourceEntity.setEditToolInfo(req.getParameter("editToolInfo"));
// //                         resourceEntity.setDescription((String) req.getParameter("description"));
// //
// //                         //还是各自有自己的 uid 更好，使用 pathUrl 进行判断。
// //                         String resUUID = UUID.randomUUID().toString();
// //                         resourceEntity.setResourceId(resUUID);
//
//                         //更新用户服务器中用户字段
//                         //计算文件的MD5值,此操作很费时间
//                         String resMd5 = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
//
//                         //参与式平台资源与用户资源字段有所不同，临时转换，后期完全统一过后，就不用了
//                         // JSONObject userBaseRes = gsmRes2UserBaseRes(resourceEntity, resMd5);
//                         JSONObject userBaseRes = new JSONObject();
//                         ArrayList resourceList = new ArrayList();
//                         resourceList.add(userBaseRes);
//
//                         //用户服务器资源
//                         String userBaseUrl = "http://" + userResServer + "/ResServer/resource";
//                         //修改userServer中用户信息,应该是和资源相关的内容，包括userId和资源有关的内容
//                         JSONObject userBaseJson = new JSONObject();
//                         userBaseJson.put("userId", uploaderId);
//                         userBaseJson.put("resources", resourceList);
//
// //                        TODO:存入数据库，token是由header携带，无法获取token
// //                        JSONObject uploadToUserServerResult = httpUtil.setUserBase(userBaseUrl, userBaseJson);
// //
// //                        if ((int) uploadToUserServerResult.get("code") != 0) {
// //                            return ResultUtils.error(-2, "Upload data failed!");
// //                        }
//
//                         //最后存入本地数据库中
//                         // IResourceEntity resDetails = resourceDao.saveResDetails(resourceEntity);
//                         //整理代码 2021/6/30
//                         IResourceEntity resDetails = null;
//
// //                        uploadInfos.uploaded.add(resDetails);
//
//                         if(!resDetails.getPathURL().equals("")){
//                             dataMap.put(entry.getKey().toString(), resDetails.getPathURL());
//                         }else {
//                             return ResultUtils.error(-2, "Upload data failed!");
//                         }
//                     }else {
// //                        文件过大
//                         return ResultUtils.error(-2, "Upload data failed!");
//                     }
//                 }else {
//                     return ResultUtils.error(-2, "Upload data failed!");
//                 }
//             }
//         } catch (Exception e) {
//             return "Fail";
//         }
//
//         return ResultUtils.success(dataMap);
//     }
}
