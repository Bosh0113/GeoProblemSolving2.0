package cn.edu.njnu.geoproblemsolving.business.activity.service;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

public interface ActivityDocService {

    JsonResult saveDocument(ActivityDoc activityDoc);

    JsonResult findDocument(String aid);

    JsonResult deleteDocument(String aid);

    JsonResult updateDocument(String aid, String document);
}
