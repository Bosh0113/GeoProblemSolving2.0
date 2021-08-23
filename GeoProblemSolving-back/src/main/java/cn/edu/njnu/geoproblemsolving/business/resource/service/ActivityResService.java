package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ActivityResService {
    Object createFolder(String folderName, String aid, ArrayList<String> paths, String userId);

    Object uploadFileInProject(HttpServletRequest req);

    Object delResource(String aid, ArrayList<String> uids, ArrayList<String> paths);

    Object putResourceByPath(String aid, ResourceEntity putRes, ArrayList<String> paths);

    JsonResult putResourceByPath(String aid, ArrayList<String> paths, String putResInfo, HttpServletRequest request) throws IOException, ServletException;

    ArrayList<ResourceEntity> getAllRes(String aid, ArrayList<String> paths);

    List<ResourceEntity> resourceToProject(String userId, String aid, String uids, ArrayList<String> paths);

    //按条件查询项目资源，在此方法进行分流
    ArrayList<ResourceEntity> searchRes(String aid, String key, String value);

    // ArrayList<ResourceEntity> searchResByType(String aid, String type);

    Object bindResToProject(ResourceEntity res, String aid);

    //资源权限控制
    Object getAllFileInProject(String aid);

    JsonResult changeResEntity(String aid, String uid, ArrayList<String> paths, HttpServletRequest req) throws IOException, ServletException;

}
