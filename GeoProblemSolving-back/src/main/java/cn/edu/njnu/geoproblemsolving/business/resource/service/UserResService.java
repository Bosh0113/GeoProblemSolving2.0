package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface UserResService {
    Object upSomeRemote();

    ResponseEntity<byte[]> downFileFromRemote(ArrayList<String> uids);

    Object searchRemote(String fileName);

    JsonResult uploadImage(HttpServletRequest request);


    /**
     * @Author zhngzhng
     * @Description 资源的相关操作
     * GSM平台这边的更新全部采用直接拿数据过来更新的方法
     * @Date 2021/4/13
     */
    ArrayList<ResourceEntity> getAllResFromService(String userId);

    Object delRes(String userId, String rid);

    JSONObject createResService(String userId, ResourceEntity resource, String paths);


    Object delResPath(String userId, String address, String rid, String paths, boolean isFolder);

    ArrayList<ResourceEntity> getResPath(String userId, String path);

    // //增加路径 效率高一些
    // Object putResByPathService(String userId, ResourceEntity res, String paths, Boolean isFolder);

    //仅通过id 相对而言，效率低一点, 当路径为 “” 的时候就只按照uid进行查找
    Object putResService(String userId, ResourceEntity res, String paths, Boolean isFolder);

    Object copyFromProject2Center(String userId, ResourceEntity copiedFile);

    JSONArray getAllFileList(String userId);

    Object searchResService(String userId, String key, String value);

    JsonResult changeFileLocation(String userId, String oldPath, String newPath, ResourceEntity resourceEntity);


}
