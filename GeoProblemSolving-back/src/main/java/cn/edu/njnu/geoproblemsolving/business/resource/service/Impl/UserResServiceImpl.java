package cn.edu.njnu.geoproblemsolving.business.resource.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.*;
import cn.edu.njnu.geoproblemsolving.business.resource.service.UserResService;
import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.user.dao.Impl.UserDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.UserEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class UserResServiceImpl implements UserResService {
    @Autowired
    UserDaoImpl userDao;

    @Value("${dataContainer}")
    String dataRemoteIp;
    @Value("${resServerIp}")
    String remoteResIp;

    private final MongoTemplate mongoTemplate;

    public UserResServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Value("${resServerIp}")
    String userResServer;

    /**
     * 单文件下载
     *
     * @param sourceStoreId
     * @return
     */
    public ResponseEntity<byte[]> downRemote(String sourceStoreId) {
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        String downRemoteUrl = "http://" + dataRemoteIp + ":8082/data/uid?=" + sourceStoreId;
        ResponseEntity<byte[]> response = httpUtil.getDownRemoteResult(downRemoteUrl);
        return response;
    }

    /**
     * 多文件下载
     *
     * @param uids 资源 id 字符串，多id逗号间隔
     * @return
     */
    public ResponseEntity<byte[]> downSomeRemote(String uids) {
        String downSomeRemoteUrl = "http://" + dataRemoteIp + ":8082/bulkDownload?oids=" + uids;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        ResponseEntity<byte[]> response = httpUtil.getDownRemoteResult(downSomeRemoteUrl);
        return response;
    }

    /**
     * 暂时先放在这里吧
     * 可以直接前端 window.open下载
     * @param uids
     * @return
     */
    @Override
    public ResponseEntity<byte[]> downFileFromRemote(ArrayList<String> uids) {
        String uidStr = uids.toString();
        if (uids.size() == 1){
            return downRemote(uidStr);
        }
        if (uids.size() == 0) return null;
        return downSomeRemote(uidStr);
    }

    @Override
    public Object upSomeRemote() {
        return null;
    }



    @Override
    public Object searchRemote(String fileName) {
        return null;
    }



    /**
     * upload image
     *
     * @param request
     * @return
     * @author mzy & zhngzhng
     */
    @Override
    public JsonResult uploadImage(HttpServletRequest request) {
        try {
            String pathURL = "Fail";
            String newFileName = "";

            // Confirm
            if (!ServletFileUpload.isMultipartContent(request)) {
                System.out.println("File is not multimedia.");
                return ResultUtils.error(-2, "File is not multimedia.");
            }

            //新建存储文件夹
            File path = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!path.exists()) {
                path = new File("");
            }
            File imgLocation = new File(path.getAbsolutePath(), "static/images/res");
            if (!imgLocation.exists()) {
                imgLocation.mkdirs();
            }

            Collection<Part> parts = request.getParts();
            for (Part part : parts) {
                if (part.getName().equals("picture")) {
                    String filePath = part.getSubmittedFileName();
                    String folderPath = imgLocation.getPath();

                    JsonResult storeResult = fileStore(part, filePath, folderPath);
                    if (storeResult.getCode() == 0)
                        newFileName = storeResult.getData().toString();
                    else
                        return storeResult;
                    pathURL = "/GeoProblemSolving/images/res/" + newFileName;
                    break;
                }
            }
            return ResultUtils.success(pathURL);
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }


    //    private static void downloadUsingStream(String urlStr) throws IOException {
//        String file = "/123.txt";
//        URL url = new URL(urlStr);
//        BufferedInputStream bis = new BufferedInputStream(url.openStream());
//        FileOutputStream fis = new FileOutputStream(file);
//        byte[] buffer = new byte[1024];
//        int count = 0;
//        while ((count = bis.read(buffer, 0, 1024)) != -1) {
//            fis.write(buffer, 0, count);
//        }
//        fis.close();
//        bis.close();
//    }

    private String copyByUrl(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        FileUtils.copyURLToFile(url, new File(savePath + File.separator + fileName));
        File file = new File(savePath + File.separator + fileName);

        Long fileSize = file.length();
        String size;
        DecimalFormat df = new DecimalFormat("##0.00");
        if (fileSize > 1024 * 1024) {
            size = df.format((float) fileSize / (float) (1024 * 1024)) + "MB";
        } else {
            size = df.format((float) fileSize / (float) (1024)) + "KB";
        }
        return size;
    }

    private JsonResult fileStore(Part part, String filePath, String folderPath) {
        try {
            String fileName = filePath.substring(0, filePath.lastIndexOf("."));
            String suffix = filePath.substring(filePath.lastIndexOf(".") + 1);
            String regexp = "[^A-Za-z_0-9\\u4E00-\\u9FA5]";
            String saveName = fileName.replaceAll(regexp, "");

            File temp = new File(folderPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            int randomNum = (int) (Math.random() * 10 + 1);
            for (int i = 0; i < 5; i++) {
                randomNum = randomNum * 10 + (int) (Math.random() * 10 + 1);
            }
            String newFileTitle = saveName + "_" + randomNum + "." + suffix;
            String localPath = temp + "/" + newFileTitle;

            File file = new File(localPath);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            InputStream inputStream = part.getInputStream();
            byte[] buffer = new byte[1024 * 1024];
            int byteRead;
            while ((byteRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileOutputStream.close();
            inputStream.close();

            return ResultUtils.success(newFileTitle);
        } catch (Exception ex) {
            return ResultUtils.error(-2, ex.toString());
        }
    }


    /**
     * @Author zhngzhng
     * @Description
     * @Date 2021/4/13
     */

    @Value("${dataContainer}")
    String dataContainerIp;

    @Value("${userServerLocation}")
    String userServerIpAndPort;

    @Override
    public ArrayList<ResourceEntity> getAllResFromService(String userId) {
        UserEntity user = userDao.findUserByIdOrEmail(userId);
        String access_token = user.getTokenInfo().getAccess_token();
        String getUrl = "http://" + userServerIpAndPort + "/auth/res";
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        JSONObject resResult = httpUtil.getRequestToServer(getUrl, access_token).getBody();
        int code = (int) resResult.get("code");
        if (code != 0) {
            return null;
        } else {
            ArrayList<ResourceEntity> resourceList = (ArrayList<ResourceEntity>) resResult.get("data");
            return resourceList;
        }
    }

    @Override
    public String delRes(String userId, String rid) {
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        String url = "http://" + userServerIpAndPort + "/auth/res/" + rid;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        JSONObject delResult = httpUtil.deleteRequestToServer(url, access_token).getBody();
        int code = (int) delResult.get("code");
        if (code != 0) {
            return "fail";
        } else {
            return "suc";
        }
    }

    /**
     * @param userId
     * @param address
     * @param rid
     * @param paths
     * @param isFolder
     * true 为 file; false 为 folder
     * @return
     */
    @Override
    public Integer delResPath(String userId, String address, String rid, String paths, boolean isFolder) {
        String delUserServerUrl = "";
        if (paths.equals("")){
            delUserServerUrl = "http://" + userServerIpAndPort + "/auth/res/" + rid;
        }else {
            delUserServerUrl = "http://" + userServerIpAndPort + "/auth/res/" + rid + "/" + paths;
        }


        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        //第一步：先删除用户服务器中字段内容
        JSONObject delUserServerResult =  httpUtil.deleteRequestToServer(delUserServerUrl, access_token).getBody();
        Integer delUserServerCode = delUserServerResult.getInteger("code");
        if (delUserServerCode != 0){
            /*
            -3 表示userServer的错误
            -4 表示dataContainer的错误
             */
            return -3;
        }
        //第二步：用户服务器中内容删除成功，用返回的内容更新GSM用户字段
        ArrayList<ResourceEntity> resArray = delUserServerResult.getObject("data", UserEntity.class).getResource();
        Update update = new Update();
        update.set("resource", resArray);
        userDao.updateInfo(userId, update);
        //完成删除
        return 0;
        /*
        只删除引用，不用删除资源实体
        第三步：删除数据容器中的资源实体,如果是文件夹就没必要
        因为数据容器错误而导致的删除失败没必要进行回滚
        这一步对用户是透明的
         */
        // if (!isFolder){
        //     String delDataInContainer  = "http://" + dataContainerIp + ":8082" + address;
        //     JSONObject delEntityResult = httpUtil.delRequest(delDataInContainer);
        //     Integer delEntityCode = delEntityResult.getInteger("code");
        //     if (delEntityCode != 1){
        //         return -4;
        //     }
        // }

    }


    @Override
    public JSONObject createResService(String userId, ResourceEntity resource, String paths) {
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        resource.setUid(UUID.randomUUID().toString());
        resource.setUploadTime(new Date());
        // MultiValueMap<String, Object> resInfo = new LinkedMultiValueMap<>();
        JSONObject resInfo = new JSONObject();
        //创建文件夹
        resInfo.put("uid", resource.getUid());
        resInfo.put("uploadTime", resource.getUploadTime());
        resInfo.put("name", resource.getName());
        resInfo.put("folder", true);
        // LinkedMultiValueMap<String, Object> folderInfo = JSONObject.parseObject(JSONObject.toJSONString(resource), LinkedMultiValueMap.class);
        String url = "http://" + userServerIpAndPort + "/auth/res/" + paths;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        Object body = httpUtil.postRequestToServer(url, access_token, resInfo).getBody();
        JSONObject createResult = JSONObject.parseObject(JSONObject.toJSONString(body), JSONObject.class);
        int code = (int) createResult.get("code");
        if (code != 0) {
            return null;
        } else {
            return resInfo;
        }
    }

    @Override
    public String putResService(String userId, ResourceEntity res, String paths, Boolean isFolder) {
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        JSONObject resInfo = new JSONObject();
        if (isFolder){
            resInfo.put("uid", res.getUid());
            resInfo.put("name", res.getName());
        }else {
            resInfo.put("uid", res.getUid());
            resInfo.put("name", res.getName());
            resInfo.put("type", res.getType());
            resInfo.put("privacy", res.getPrivacy());
            resInfo.put("description", res.getDescription());
        }
        //请求地址
        String url = "";
        if (paths.equals("")){
            url = "http://" + userServerIpAndPort + "/auth/res";
        }else {
            url = "http://" + userServerIpAndPort + "/auth/res/" + paths;
        }

        RestTemplateUtil httpUtil = new RestTemplateUtil();
        JSONObject putResult = httpUtil.putRequestToServer(url, access_token, resInfo).getBody();
        int code =  putResult.getInteger("code");
        if (code != 0) {
            return "fail";
        } else {
            //更新成功，更新本地数据资源
            ArrayList<ResourceEntity> putRes = putResult.getObject("data", UserEntity.class).getResource();
            //这有问题,转过去转过来的会导致某些数据类型失去原有属性
            // ArrayList<ResourceEntity> uploadedRes = JSONObject.parseObject(JSONObject.toJSONString(uploadedResJson), ArrayList.class);
            Update update = new Update();
            update.set("resource", putRes);
            userDao.updateInfo(userId, update);
            return "suc";
        }
    }

    //上传文件
    public Object upRemote(HttpServletRequest req) {
        IUploadResult uploadInfos = new IUploadResult();
        //记录上传状态
        uploadInfos.failed = new ArrayList<>();
        uploadInfos.sizeOver = new ArrayList<>();
        uploadInfos.uploaded = new ArrayList<>();
        try {
            if (!ServletFileUpload.isMultipartContent(req)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            HttpSession session = req.getSession();
            String userId = (String) session.getAttribute("userId");
            String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();

            Collection<Part> parts = req.getParts();
            int fileNum = parts.size() - 4;
            //post payLoad存储，使用LinkedMultiValueMap<String, Object>key/value形式进行存储
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            //restTemplate工具类
            RestTemplateUtil httpUtil = new RestTemplateUtil();
            for (Part part : parts) {
                try {
                    if (part.getName().equals("file")) {
                        //用于标记文件上传进度
                        fileNum--;
                        if (part.getSize() < 1024 * 1024 * 1024) {
                            //将数据上传到数据容器
                            String file = part.getSubmittedFileName();
                            String temp[] = file.split("\\.");
                            String fileName = temp[0];
                            String suffix = "." + temp[1];

                            File fileTemp = File.createTempFile(UUID.randomUUID().toString(), suffix);//创建临时文件
                            FileUtils.copyInputStreamToFile(part.getInputStream(), fileTemp);
                            // MultipartFile multipartFile = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), part.getInputStream());
                            FileSystemResource multipartFile = new FileSystemResource(fileTemp);      //临时文件

                            valueMap.add("datafile", multipartFile);
                            valueMap.add("name", fileName);
                            String uploadRemoteUrl = "http://" + dataContainerIp + ":8082/data";
                            //向dataContainer传输数据
                            JSONObject uploadRemoteResult = httpUtil.postRequestMap(uploadRemoteUrl, valueMap).getBody();
                            Integer uploadResultInfo = uploadRemoteResult.getInteger("code");
                            String dataIdInContainer = uploadRemoteResult.getJSONObject("data").getString("id");

                            if (!uploadResultInfo.equals(1)) {
                                uploadInfos.failed.add(fileName);
                                valueMap.clear();
                                continue;
                            }
                            //成功将资源上传到数据容器中
                            // 接下来将资源基本信息写入本地数据库及用户服务器
                            String fileSize;
                            DecimalFormat df = new DecimalFormat("##0.00");
                            if (part.getSize() > 1024 * 1024) {
                                fileSize = df.format((float) part.getSize() / (float) (1024 * 1024)) + "MB";
                            } else {
                                fileSize = df.format((float) part.getSize() / (float) (1024)) + "KB";
                            }
                            ResourceEntity res = new ResourceEntity();
                            res.setUid(UUID.randomUUID().toString());
                            res.setName(fileName);
                            res.setSuffix(suffix);
                            res.setUploadTime(new Date());
                            res.setFileSize(fileSize);
                            res.setPrivacy(req.getParameter("privacy"));
                            res.setType(req.getParameter("type"));
                            res.setDescription(req.getParameter("description"));
                            res.setFolder(false);
                            res.setUserUpload(true);
                            String address = "http://" + dataContainerIp + ":8082" + "/data/" + dataIdInContainer;
                            res.setAddress(address);

                            String paths = req.getParameter("paths");
                            String url = "http://" + userServerIpAndPort + "/auth/res/" + paths;
                            JSONObject resJson = JSONObject.parseObject(JSONObject.toJSONString(res));
                            JSONObject uploadResult = httpUtil.postRequestToServer(url, access_token, resJson).getBody();
                            Integer code = uploadResult.getInteger("code");
                            if (code != 0) {
                                //用户服务器更新失败，回滚上传操作
                                String delUrl = "http://" + dataContainerIp + ":8082" + address;
                                httpUtil.delRequest(delUrl);
                                uploadInfos.failed.add(part.getSubmittedFileName());
                                continue;
                            }
                            uploadInfos.uploaded.add(res);
                            //更新本地用户
                            if (fileNum > 0) {
                                continue;
                            }
                            //最后一个上传完成后，一次性更新用户
                            ArrayList<ResourceEntity> uploadedRes = uploadResult.getObject("data", UserEntity.class).getResource();
                            //这有问题,转过去转过来的会导致某些数据类型失去原有属性
                            // ArrayList<ResourceEntity> uploadedRes = JSONObject.parseObject(JSONObject.toJSONString(uploadedResJson), ArrayList.class);
                            Update update = new Update();
                            update.set("resource", uploadedRes);
                            userDao.updateInfo(userId, update);
                        } else {
                            uploadInfos.sizeOver.add(part.getSubmittedFileName());
                        }
                    }
                } catch (Exception e) {
                    uploadInfos.failed.add(part.getSubmittedFileName());
                }
            }
            return uploadInfos;

        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public ArrayList<ResourceEntity> getResPath(String userId, String paths) {
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        String url = "http://" + userServerIpAndPort + "/auth/res/" + paths;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        JSONObject getResult = httpUtil.getRequestToServer(url, access_token).getBody();
        if (getResult.getInteger("code") != 0){
            return null;
        }
        //探索空文件夹
        ArrayList<ResourceEntity> res = getResult.getObject("data", ArrayList.class);
        return res;
    }

    @Override
    public String copyFromProject2Center(String userId, ResourceEntity copiedFile) {
        //实际上只是将数据添加到用户数据库中
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        String url = "http://" + userServerIpAndPort + "/auth/res/0";
        RestTemplateUtil httpUntil = new RestTemplateUtil();
        JSONObject copiedFilJson = JSONObject.parseObject(JSONObject.toJSONString(copiedFile));
        JSONObject copiedResultJson = httpUntil.postRequestToServer(url, access_token, copiedFilJson).getBody();
        int code = copiedResultJson.getInteger("code");
        if (code != 0){
            return "fail";
        }
        ArrayList<ResourceEntity> resList = copiedResultJson.getObject("data", UserEntity.class).getResource();
        //更新本地用户资源
        Update update = new Update();
        update.set("resource", resList);
        userDao.updateInfo(userId, update);
        return "suc";
    }

    @Override
    public JSONArray getAllFileList(String userId) {
        String access_token = userDao.findUserByIdOrEmail(userId).getTokenInfo().getAccess_token();
        String url = "http://" + userServerIpAndPort + "/auth/res/file/all";
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        JSONObject getAllResult = httpUtil.getRequestToServer(url, access_token).getBody();
        return getAllResult.getJSONArray("data");
    }

    //
    @Override
    public Object searchResService(String userId, String key, String value) {
        return null;
    }
}
