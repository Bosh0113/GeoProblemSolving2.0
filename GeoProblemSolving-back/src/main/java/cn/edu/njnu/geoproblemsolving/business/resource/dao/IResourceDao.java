package cn.edu.njnu.geoproblemsolving.business.resource.dao;

import cn.edu.njnu.geoproblemsolving.Entity.Resources.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;

import java.util.ArrayList;
import java.util.Map;

public interface IResourceDao {
    String UploadRemote(LinkedMultiValueMap<String, Object> paramMap);
    IResourceEntity saveResDetails(IResourceEntity resourceEntity);
    ResponseEntity downSomeRemote(ArrayList<String> sourceStoreIds);
    Object deleteRemote(String uid);
    Object deleteSomeRemote(ArrayList<String> oids);
    Object delResById(String resId);
    JsonResult inquiryResource(Map<String, String> inquiryKeyAndValue);
    Object delResByPathUrl(String pathUrl);
    // Object delResByIds(ArrayList<String> resIds);
}
