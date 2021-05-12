package cn.edu.njnu.geoproblemsolving.business.personalTask.dao;

import cn.edu.njnu.geoproblemsolving.business.personalTask.entity.PersonalTaskEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

public interface PTaskDao {
    JsonResult findByUserId(String userId);
    JsonResult findById(String ptId);
    JsonResult delById(String ptId);
    JsonResult updateTask(JSONObject taskJson);
    JsonResult createTask(PersonalTaskEntity personalTask);
}
