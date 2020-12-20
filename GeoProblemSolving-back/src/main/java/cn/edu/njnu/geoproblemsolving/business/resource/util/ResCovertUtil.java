package cn.edu.njnu.geoproblemsolving.business.resource.util;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.IResourceEntity;
import com.alibaba.fastjson.JSONObject;

public class ResCovertUtil {
    public JSONObject gsmRes2UserBaseRes(IResourceEntity resourceEntity, String resMd5){
        JSONObject userBaseRes = new JSONObject();
        userBaseRes.put("uid", resourceEntity.getResourceId());
        userBaseRes.put("name", resourceEntity.getName());
        userBaseRes.put("type", resourceEntity.getType());
        userBaseRes.put("thumbnail",resourceEntity.getThumbnail());
        userBaseRes.put("editToolInfo", resourceEntity.getEditToolInfo());
        userBaseRes.put("fileSize", resourceEntity.getFileSize());
        userBaseRes.put("address", "dataContainer");
        userBaseRes.put("parent", "no info");
        userBaseRes.put("md5", resMd5);
        userBaseRes.put("privacy", resourceEntity.getPrivacy());
        userBaseRes.put("suffix", "");
        userBaseRes.put("description", resourceEntity.getDescription());
        userBaseRes.put("template", "");
        userBaseRes.put("uploadTime", resourceEntity.getUploadTime());
        userBaseRes.put("children", null);
        return userBaseRes;
    }
}
