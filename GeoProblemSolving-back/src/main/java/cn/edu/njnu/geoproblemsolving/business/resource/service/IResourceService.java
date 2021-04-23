package cn.edu.njnu.geoproblemsolving.business.resource.service;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.AddIResourceDTO;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.UpdateResult;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Map;

public interface IResourceService {
    Object uploadRemote(HttpServletRequest request);

    Object upSomeRemote();

    Object downloadRemote(String uid);

    Object downSomeRemote(ArrayList<String> oids);

    JsonResult deleteRemote(String uid, String rid);

    Object delSomeRemote(String uid, ArrayList<String> oids);

    Object searchRemote(String fileName);

    JsonResult inquiryLocal(Map<String, String> filedAndValue);

    Object saveResource(AddIResourceDTO add) throws IOException, URISyntaxException;

    JsonResult uploadImage(HttpServletRequest request);


    ArrayList<ResourceEntity> getRes(ArrayList<String> rids);

    UpdateResult updateRes(String rid, IResourceEntity resource);

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

}
