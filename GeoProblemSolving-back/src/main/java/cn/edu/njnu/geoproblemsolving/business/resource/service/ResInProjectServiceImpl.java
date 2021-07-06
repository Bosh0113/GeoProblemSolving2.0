package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.dao.IIResourceDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IUploadResult;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.util.RestTemplateUtil;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @ClassName ResInProjectServiceImpl
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/4/20
 **/
@Service
public class ResInProjectServiceImpl implements ResourceInProjectService {
    @Autowired
    IUserImpl userDao;

    @Autowired
    IIResourceDaoImpl resDao;

    @Value("${dataContainer}")
    String dataContainerIp;

    @Override
    public JSONObject createFolder(String folderName, String aid, ArrayList<String> paths, String userId) {
        User creator = userDao.findUserByIdOrEmail(userId);
        ResourceEntity folder = new ResourceEntity();
        folder.setUid(UUID.randomUUID().toString());
        folder.setName(folderName);
        folder.setFolder(true);
        folder.setUploadTime(new Date());
        folder.setChildren(new ArrayList<>());
        folder.setUploaderId(userId);
        folder.setUploaderName(creator.getName());
        folder.setActivityId(aid);

        JSONObject folderJson = new JSONObject();
        folderJson.put("uid", folder.getUid());
        folderJson.put("uploadTime", folder.getUploadTime());
        folderJson.put("name", folder.getName());
        folderJson.put("folder", true);
        //若为根目录则直接存入即可
        if (paths.get(0).equals("0") && paths.size() == 1) {
            resDao.addResource(folder);
            return folderJson;
        }
        //不是根目录则需要通过 aid 查询 resource 然后按路径进行递归存入，然后采用更新进行更新
        List<ResourceEntity> resourceList = resDao.queryByAid(aid);
        ArrayList<ResourceEntity> folders = new ArrayList<>();
        folders.add(folder);
        String rootResUid = paths.get(paths.size() - 1);
        //返回的是
        List<ResourceEntity> putResList = aRes(resourceList, paths, folders, "0");
        Update update = new Update();
        Query query = new Query(Criteria.where("uid").is(rootResUid));
        for (int i = 0; i < putResList.size(); i++) {
            ResourceEntity res = putResList.get(i);
            if (res.getUid().equals(rootResUid)) {
                update.set("children", res.getChildren());
                break;
            }
        }
        String crateResult = resDao.updateRes(query, update);
        if (crateResult.equals("suc")) {
            return folderJson;
        }
        return null;
    }


    @Override
    public Object uploadFileInProject(HttpServletRequest req) {
        IUploadResult uploadInfos = new IUploadResult();
        //记录上传状态
        uploadInfos.failed = new ArrayList<>();
        uploadInfos.sizeOver = new ArrayList<>();
        uploadInfos.uploaded = new ArrayList<ResourceEntity>();
        try {
            if (!ServletFileUpload.isMultipartContent(req)) {
                System.out.println("File is not multimedia.");
                return "Fail";
            }
            HttpSession session = req.getSession();
            String userId = (String) session.getAttribute("userId");
            String uploadName = userDao.findUserByIdOrEmail(userId).getName();
            String aid = req.getParameter("aid");
            List<ResourceEntity> resourceList = resDao.queryByAid(aid);
            String[] pathsArray = req.getParameter("paths").split(",");
            ArrayList<String> paths = new ArrayList<>();
            for (String path : pathsArray) {
                paths.add(path);
            }


            Collection<Part> parts = req.getParts();
            //多个肯定是在同一个文件夹的可以直接存入
            //确定request 中的文件数量
            int fileNum = 0;
            for (Part part : parts) {
                if (part.getName().equals("file")) {
                    fileNum++;
                }
            }

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
                            // 接下来将资源存入GSM数据库中
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
                            res.setUploaderId(userId);
                            res.setUploaderName(uploadName);
                            res.setActivityId(aid);
                            try {
                                String thumbnail = req.getParameter("thumbnail");
                                String editToolInfo = req.getParameter("editToolInfo");
                                res.setThumbnail(thumbnail);
                                res.setEditToolInfo(editToolInfo);
                            }
                            catch (Exception ex){

                            }

                            uploadInfos.uploaded.add(res);
                            //如果不是最后一个，则进入下一次循环
                            if (fileNum != 0) {
                                continue;
                            }
                            //批量上传至于最后一个资源，开始将资源存入字段中
                            String rootResUid = paths.get(paths.size() - 1);
                            //如果为根目录直接存入表中
                            if (rootResUid.equals("0") && paths.size() == 1) {
                                for (ResourceEntity resItem : uploadInfos.uploaded) {
                                    resDao.addResource(resItem);
                                }
                                break;
                            }
                            List<ResourceEntity> putResList = aRes(resourceList, paths, uploadInfos.uploaded, "0");
                            int index = 0;
                            for (int i = 0; i < putResList.size(); i++) {
                                ResourceEntity item = putResList.get(i);
                                if (item.getUid().equals(rootResUid)) {
                                    index = i;
                                    break;
                                }
                            }

                            Query query = new Query(Criteria.where("uid").is(rootResUid));
                            Update update = new Update();
                            update.set("children", putResList.get(index).getChildren());
                            //不会出问题
                            resDao.updateRes(query, update);

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

    private List<ResourceEntity> aRes(List<ResourceEntity> resList, ArrayList<String> paths, List<ResourceEntity> resources, String parent) {
        if (paths.size() == 0 || paths.get(0).equals("0")) {
            for (ResourceEntity resource : resources) {
                resource.setParent(parent);
                resList.add(resource);
            }
        } else {
            String path = paths.remove(paths.size() - 1);
            for (int i = 0; i < resList.size(); i++) {
                ResourceEntity res = resList.get(i);
                if (res.getFolder()) {
                    if (res.getUid().equals(path)) {
                        aRes(res.getChildren(), paths, resources, path);
                        resList.set(i, res);
                        break;
                    }
                } else {
                    //不是文件夹，无 children 一说，直接跳过
                    continue;
                }
            }
        }
        return resList;
    }

    @Override
    public String delResource(String aid, ArrayList<String> uids, ArrayList<String> paths) {
        //批量删除也只能删除一个文件夹内容的
        if (paths.get(0).equals("0") && paths.size() == 1) {
            for (String uid : uids) {
                resDao.delResource(uid);
            }
            return "suc";
        }
        //不是根目录情况则需要遍历删除
        ArrayList<ResourceEntity> resInProjectList = (ArrayList<ResourceEntity>) resDao.queryByAid(aid);
        String rootResUid = paths.get(paths.size() - 1);
        ArrayList<ResourceEntity> putResList = dResource(resInProjectList, uids, paths);
        Query query = new Query(Criteria.where("uid").is(rootResUid));
        Update update = new Update();

        for (int i = 0; i < putResList.size(); i++) {
            ResourceEntity item = putResList.get(i);
            if (item.getUid().equals(rootResUid)) {
                update.set("children", putResList.get(i).getChildren());
                break;
            }
        }

        return resDao.updateRes(query, update);
    }

    //提供批量删除功能
    private ArrayList<ResourceEntity> dResource(ArrayList<ResourceEntity> resInProjectList, ArrayList<String> uids, ArrayList<String> paths) {
        if (paths.size() == 0 || paths.get(0).equals("0")) {
            ArrayList<Integer> delIndex = new ArrayList<Integer>();
            for (int j = 0; j < resInProjectList.size(); j++) {
                ResourceEntity res = resInProjectList.get(j);
                for (int k = 0; k < uids.size(); k++) {
                    String uid = uids.get(k);
                    if (res.getUid().equals(uid)) {
                        delIndex.add(j);
                    }
                }
            }
            Collections.sort(delIndex);
            int num = 0;
            for (Integer index : delIndex) {
                resInProjectList.remove(index - num);
                num++;
            }
        } else {
            String path = paths.remove(paths.size() - 1);
            for (int i = 0; i < resInProjectList.size(); i++) {
                ResourceEntity resource = resInProjectList.get(i);
                if (resource.getFolder()) {
                    if (resource.getUid().equals(path)) {
                        dResource(resource.getChildren(), uids, paths);
                    }
                } else {
                    continue;
                }
            }
        }
        return resInProjectList;

    }

    private ArrayList<ResourceEntity> dRes(ArrayList<ResourceEntity> rootRes, String uid, ArrayList<String> paths) {
        if (paths.size() == 0 || paths.get(0).equals("0")) {
            for (int i = 0; i < rootRes.size(); i++) {
                ResourceEntity resource = rootRes.get(i);
                if (resource.getUid().equals(uid)) {
                    rootRes.remove(i);
                }
            }
        } else {
            String path = paths.remove(paths.size() - 1);
            for (int j = 0; j < rootRes.size(); j++) {
                ResourceEntity res = rootRes.get(j);
                if (res.getFolder()) {
                    if (res.getUid().equals(path)) {
                        dRes(res.getChildren(), uid, paths);
                        rootRes.set(j, res);
                    }
                } else {
                    continue;
                }

            }
        }
        return rootRes;
    }

    @Override
    public String putResourceByPath(String aid, ResourceEntity putRes, ArrayList<String> paths) {
        if (paths.size() == 1 && paths.get(0).equals("0")) {
            String delResUid = putRes.getUid();
            ResourceEntity res = resDao.queryByUid(delResUid);
            String[] nullPropertyNames = getNullPropertyNames(putRes);
            BeanUtils.copyProperties(putRes, res, nullPropertyNames);
            resDao.addResource(res);
            return "suc";
        }
        ArrayList<ResourceEntity> rootResList = (ArrayList<ResourceEntity>) resDao.queryByAid(aid);
        String rootResUid = paths.get(paths.size() - 1);
        ArrayList<ResourceEntity> putResList = pResourceByPath(rootResList, putRes, paths);
        Query query = new Query(Criteria.where("uid").is(rootResUid));
        Update update = new Update();
        for (int i = 0; i < putResList.size(); i++) {
            ResourceEntity item = putResList.get(i);
            if (item.getUid().equals(rootResUid)) {
                update.set("children", item.getChildren());
                break;
            }
        }
        return resDao.updateRes(query, update);
    }

    private ArrayList<ResourceEntity> pResourceByUid(ArrayList<ResourceEntity> rootResList, ResourceEntity putRes) {
        if (rootResList.size() != 0 || rootResList != null) {
            for (int i = 0; i < rootResList.size(); i++) {
                ResourceEntity res = rootResList.get(i);
                if (res.getUid().equals(putRes.getUid())) {
                    String[] nullPropertyNames = getNullPropertyNames(putRes);
                    BeanUtils.copyProperties(putRes, res, nullPropertyNames);
                    rootResList.remove(i);
                    rootResList.add(i, res);
                    break;
                } else {
                    if (res.getFolder()) {
                        pResourceByUid(res.getChildren(), putRes);
                    } else {
                        continue;
                    }
                }
            }
        }
        return rootResList;
    }

    private ArrayList<ResourceEntity> pResourceByPath(ArrayList<ResourceEntity> rootResList, ResourceEntity putRes, ArrayList<String> paths) {
        //此判断条件确定如果不是根目录的话，前面不能加0
        if (paths.size() == 0 || paths.get(0).equals("0")) {
            for (int i = 0; i < rootResList.size(); i++) {
                ResourceEntity res = rootResList.get(i);
                if (res.getUid().equals(putRes.getUid())) {
                    String[] nullPropertyNames = getNullPropertyNames(putRes);
                    BeanUtils.copyProperties(putRes, res, nullPropertyNames);
                    rootResList.remove(i);
                    rootResList.add(i, res);
                }
            }
        } else {
            String path = paths.remove(paths.size() - 1);
            for (int j = 0; j < rootResList.size(); j++) {
                ResourceEntity resItem = rootResList.get(j);
                if (resItem.getUid().equals(path)) {
                    pResourceByPath(resItem.getChildren(), putRes, paths);
                    rootResList.set(j, resItem);
                }
            }
        }
        return rootResList;
    }


    //获取空字段名
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object propertyValue = src.getPropertyValue(pd.getName());
            if (propertyValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    @Override
    public ArrayList<ResourceEntity> getAllRes(String aid, ArrayList<String> paths) {
        ArrayList<ResourceEntity> resList = (ArrayList<ResourceEntity>) resDao.queryByAid(aid);
        return gAllRes(resList, paths);
    }

    private ArrayList<ResourceEntity> gAllRes(ArrayList<ResourceEntity> resInProjectList, ArrayList<String> paths) {
        //退出递归条件：paths 为空， 或者paths长度为1且值为“0”此情况表示为根目录
        ArrayList<ResourceEntity> resList = new ArrayList<ResourceEntity>();
        if (paths.size() == 0 || paths.get(0).equals("0")) {
            return resInProjectList;
        } else {
            String path = paths.remove(paths.size() - 1);
            for (int i = 0; i < resInProjectList.size(); i++) {
                ResourceEntity resource = resInProjectList.get(i);
                if (resource.getUid().equals(path)) {
                    resList = gAllRes(resource.getChildren(), paths);
                }
            }
        }
        return resList;
    }

    /*
    资源直接分享到项目根文件夹中
    * @Author zhngzhng
    * @Description Todo 
    * @Param [userId, aid, uids]
    * @Date 2021/4/21
    */
    @Value("${userServerLocation}")
    String userServerLocation;

    @Override
    public ArrayList<ResourceEntity> resourceToProject(String userId, String aid, String uids, ArrayList<String> paths) {
        User sharer = userDao.findUserByIdOrEmail(userId);
        String access_token = sharer.getTokenInfo().getAccess_token();
        String sharerName = sharer.getName();
        String url = "http://" + userServerLocation + "/auth/res/s/" + uids;
        RestTemplateUtil httpUtil = new RestTemplateUtil();
        Object[] pathArray = paths.toArray();
        try {
            //在用户服务器根据 uid 查询资源
            JSONObject getResult = httpUtil.getRequestToServer(url, access_token).getBody();
            assert getResult != null;

            JSONArray fileList = getResult.getJSONArray("data");
            List<ResourceEntity> rootResList = resDao.queryByAid(aid);
            ArrayList<ResourceEntity> resList = new ArrayList<>();

            ArrayList<String> path = new ArrayList<>();
            for (Object item : pathArray) {
                path.add(item.toString());
            }
            //将获取到的资源转换为 gsm 的资源，并将其存入对应的项目中
            for (Object item : fileList) {
                ResourceEntity resourceEntity = JSONObject.parseObject(JSONObject.toJSONString(item), ResourceEntity.class);
                resourceEntity.setActivityId(aid);
                resourceEntity.setUploaderId(userId);
                resourceEntity.setUploaderName(sharerName);
                if (path.size() == 1 && path.get(0).equals("0")) {
                    resDao.addResource(resourceEntity);
                }
                resList.add(resourceEntity);
            }
            if (path.size() == 1 && path.get(0).equals("0")) {
                return resList;
            }
            String rootResUid = path.get(path.size() - 1);
            //如果不是根目录则存入对应的文件夹下
            List<ResourceEntity> putResList = aRes(rootResList, path, resList, "0");
            Query query = new Query(Criteria.where("uid").is(rootResUid));
            Update update = new Update();
            for (ResourceEntity item : putResList) {
                if (item.getUid().equals(rootResUid)) {
                    update.set("children", item.getChildren());
                    break;
                }
            }
            resDao.updateRes(query, update);
            return resList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ArrayList<ResourceEntity> searchRes(String aid, String key, String value) {
        List<ResourceEntity> projectRes = resDao.queryByAid(aid);
        ArrayList<ResourceEntity> returnRes = Lists.newArrayList();
        if (key.equals("type")) {
            //通过 type 进行查询
            return sResByType(value, projectRes, returnRes);
        } else if (key.equals("privacy")) {
            //通过 privacy 进行查询
            return sResByPrivacy(value, projectRes, returnRes);
        }
        return null;
    }

    private ArrayList<ResourceEntity> sResByType(String type, List<ResourceEntity> projectRes, ArrayList<ResourceEntity> choseRes) {
        for (int i = 0; i < projectRes.size(); i++) {
            ResourceEntity res = projectRes.get(i);
            if (res.getFolder()) {
                sResByType(type, res.getChildren(), choseRes);
            } else {
                if (res.getType().equals(type)) {
                    choseRes.add(res);
                }
            }
        }
        return choseRes;
    }

    private ArrayList<ResourceEntity> sResByPrivacy(String privacy, List<ResourceEntity> projectRes, ArrayList<ResourceEntity> choseRes) {
        for (int i = 0; i < projectRes.size(); i++) {
            ResourceEntity res = projectRes.get(i);
            if (res.getFolder()) {
                sResByType(privacy, res.getChildren(), choseRes);
            } else {
                if (res.getPrivacy().equals(privacy)) {
                    choseRes.add(res);
                }
            }
        }
        return choseRes;
    }

    @Override
    public String bindResToProject(ResourceEntity res, String aid) {
        res.setUid(UUID.randomUUID().toString());
        res.setUploadTime(new Date());
        String fullName = res.getName();
        String name = fullName.split("\\.")[0];
        String suffix = fullName.split("\\.")[1];
        res.setSuffix(suffix);
        res.setActivityId(aid);
        res.setUserUpload(false);
        res.setName(name);
        ResourceEntity addResource = resDao.addResource(res);
        if (addResource == null) {
            return "fail";
        }
        //直接存储
        return "suc";
    }

    @Override
    public ArrayList<ResourceEntity> getAllFileInProject(String aid) {
        List<ResourceEntity> resList = resDao.queryByAid(aid);
        ArrayList<ResourceEntity> fileList = Lists.newArrayList();
        ArrayList<ResourceEntity> fileInProjectList = gallFileInProject(resList, fileList);
        return fileInProjectList;
    }

    private ArrayList<ResourceEntity> gallFileInProject(List<ResourceEntity> resInProject, ArrayList<ResourceEntity> fileList) {
        for (int i = 0; i < resInProject.size(); i++) {
            ResourceEntity res = resInProject.get(i);
            if (res.getFolder()){
                gallFileInProject(res.getChildren(), fileList);
            }else {
                fileList.add(res);
            }
        }
        return fileList;
    }
}
