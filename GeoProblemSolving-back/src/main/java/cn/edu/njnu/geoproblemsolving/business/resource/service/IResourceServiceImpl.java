package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.dao.IResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IUploadResult;
import cn.edu.njnu.geoproblemsolving.business.resource.util.ResCovertUtil;
import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.DeleteResult;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Service
public class IResourceServiceImpl implements IResourceService {
    @Autowired
    IResourceDaoImpl resourceDao;
    @Value("${dataContainerIpAndPort_minyuan}")
    String dataRemoteIp;

    /**
     * 文件上传，涉及到三个地方：
     * 1. dataContainer
     * 2. Local database
     * 3. userBase(UserServer)
     * @param req
     * @return
     */
    public Object uploadRemote(HttpServletRequest req) {
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
            Collection<Part> parts = req.getParts();
            //post payLoad存储，使用LinkedMultiValueMap<String, Object>key/value形式进行存储
            LinkedMultiValueMap<String, Object> valueMap = new LinkedMultiValueMap<>();
            //restTemplate工具类
            RestTemplateUtil httpUtil = new RestTemplateUtil();
            for (Part part : parts) {
                try {
                    if (part.getName().equals("file")) {
                        if (part.getSize() < 1024 * 1024 * 1024) {
                            String fileName = part.getSubmittedFileName();
                            MultipartFile multipartFile = new MockMultipartFile(ContentType.APPLICATION_OCTET_STREAM.toString(), part.getInputStream());
                            valueMap.add("datafile", multipartFile.getResource());
                            valueMap.add("name", fileName);
                            valueMap.add("userId", session.getAttribute("userId"));
                            valueMap.add("serverNode", "china");
                            valueMap.add("origination", "GeoProblemSolving");
                            String uploadRemoteUrl = "http://" + dataRemoteIp + "/data";
                            //向dataContainer传输数据
                            JSONObject uploadRemoteResult = httpUtil.uploadRemote(uploadRemoteUrl, valueMap);
                            String uploadResultInfo = (String)uploadRemoteResult.get("msg");
                            if (!uploadResultInfo.equals("success")) {
                                uploadInfos.failed.add(fileName);
                                valueMap.clear();
                                continue;
                            }
                            //上传成功，将资源基本信息写入本地数据库及用户服务器
                            String fileSize;
                            DecimalFormat df = new DecimalFormat("##0.00");
                            if (part.getSize() > 1024 * 1024) {
                                fileSize = df.format((float) part.getSize() / (float) (1024 * 1024)) + "MB";
                            } else {
                                fileSize = df.format((float) part.getSize() / (float) (1024)) + "KB";
                            }
                            Date uploadDate = new Date();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                            String uploadTime = simpleDateFormat.format(uploadDate);

                            String uploaderId = (String) session.getAttribute("userId");
                            String uploaderName = (String) session.getAttribute("userName");
                            IResourceEntity resourceEntity = new IResourceEntity();
                            resourceEntity.setName(fileName);
                            resourceEntity.setFileSize(fileSize);
                            resourceEntity.setUploaderId(uploaderId);
                            resourceEntity.setUploaderName(uploaderName);
                            resourceEntity.setUploadTime(uploadTime);
                            resourceEntity.setPrivacy(req.getParameter("privacy"));
                            resourceEntity.setType(req.getParameter("type"));

                            resourceEntity.setEditToolInfo(req.getParameter("editToolInfo"));
                            resourceEntity.setDescription((String) req.getParameter("description"));
                            String resUUID = UUID.randomUUID().toString();
                            resourceEntity.setResourceId(resUUID);
                            IResourceEntity resDetails = resourceDao.saveResDetails(resourceEntity);
                            String userBaseUrl = "http://localhost:8090/ResServer/user/updateJson";
                            //修改userServer中用户信息,应该是和资源相关的内容，包括userId和资源有关的内容
                            JSONObject userBaseJson = new JSONObject();
                            //计算文件的MD5值,此操作很费时间
                            String resMd5 = DigestUtils.md5DigestAsHex(multipartFile.getInputStream());
                            ResCovertUtil resCovertUtil = new ResCovertUtil();
                            JSONObject userBaseRes = resCovertUtil.gsmRes2UserBaseRes(resourceEntity, resMd5);
                            userBaseJson.put("userId", uploaderId);
                            userBaseJson.put("resources", userBaseRes);
                            httpUtil.setUserBase(userBaseUrl, userBaseJson);
                            uploadInfos.uploaded.add(resDetails);
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

    /**
     * 单文件下载
     *
     * @param sourceStoreId
     * @return
     */
    public ResponseEntity<byte[]> downloadRemote(String sourceStoreId) {
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        String downRemoteUrl = "http://" + dataRemoteIp + "/data/uid?=" + sourceStoreId;
        ResponseEntity<byte[]> response = httpUtil.getDownRemoteResult(downRemoteUrl);
        return response;
    }

    /**
     * 多文件下载
     *
     * @param sourceStoreIds
     * @return
     */
    public ResponseEntity<byte[]> downSomeRemote(ArrayList<String> sourceStoreIds) {
        String oids = "";
        for (int i = 0; i < sourceStoreIds.size(); i++) {
            String oid = sourceStoreIds.get(i);
            if (i != sourceStoreIds.size() - 1) {
                oids += oid + ",";
                continue;
            }
            oids += oid;
        }
        String downSomeRemoteUrl = "http://" + dataRemoteIp + "/bulkDownload?oids=" + oids;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        ResponseEntity<byte[]> response = httpUtil.getDownRemoteResult(downSomeRemoteUrl);
        return response;
    }

    @Override
    public Object upSomeRemote() {
        return null;
    }

    @Override
    public Object deleteRemote(String uid) {
        String delRemoteUrl = "http://" + dataRemoteIp + "/del?uid=" + uid;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        ResponseEntity response = httpUtil.getDelRemoteResult(delRemoteUrl);
        //远程删除成功，开始删除本地数据库中的内容
        DeleteResult delResByResult = (DeleteResult) resourceDao.delResById(uid);
        return response.getBody();
    }

    @Override
    public Object delSomeRemote(ArrayList<String> sourceStoreIds) {
        String oids = "";
        for (int i = 0; i < sourceStoreIds.size(); i++) {
            String oid = sourceStoreIds.get(i);
            //删除本地数据库中的资源详情数据
            String delResult = (String)resourceDao.delResById(oid);
            if (delResult.equals("0")){
                return "Fail.The local database doesn't have "+ oid;
            }
            if (i != sourceStoreIds.size() - 1) {
                oids += oid + ",";
            } else {
                oids += oid;
            }
        }
        String delSomeRemoteUrl = "http://" + dataRemoteIp + "/bulkDel?oids=" + oids;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        ResponseEntity response = httpUtil.getDelRemoteResult(delSomeRemoteUrl);
        return response.getBody();
    }

    @Override
    public Object searchRemote(String fileName) {
        return null;
    }

}
