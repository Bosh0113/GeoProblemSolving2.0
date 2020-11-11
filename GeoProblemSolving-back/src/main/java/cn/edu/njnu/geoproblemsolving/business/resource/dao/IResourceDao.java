package cn.edu.njnu.geoproblemsolving.business.resource.dao;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;

public interface IResourceDao {
    String UploadRemote(LinkedMultiValueMap<String, Object> paramMap);
    IResourceEntity saveResDetails(IResourceEntity resourceEntity);
    ResponseEntity downSomeRemote(ArrayList<String> sourceStoreIds);
    Object deleteRemote(String uid);
    Object deleteSomeRemote(ArrayList<String> oids);
    Object delResById(String resId);
    // Object delResByIds(ArrayList<String> resIds);
}
