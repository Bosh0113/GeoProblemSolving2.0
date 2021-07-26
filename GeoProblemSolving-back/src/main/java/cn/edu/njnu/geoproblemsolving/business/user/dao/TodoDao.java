package cn.edu.njnu.geoproblemsolving.business.user.dao;

import cn.edu.njnu.geoproblemsolving.business.user.entity.TodoEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

public interface TodoDao {
    JsonResult findByUserId(String userId);
    JsonResult findById(String ptId);
    JsonResult delById(String ptId);
    JsonResult updateTask(JSONObject taskJson);
    JsonResult createTask(TodoEntity personalTask);
}
