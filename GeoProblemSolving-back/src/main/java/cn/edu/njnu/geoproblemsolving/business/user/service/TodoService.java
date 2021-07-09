package cn.edu.njnu.geoproblemsolving.business.user.service;

import cn.edu.njnu.geoproblemsolving.business.user.entity.TodoEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

public interface TodoService {
    JsonResult findPersonalTask(String userId);
    JsonResult createPersonalTask(TodoEntity todoEntity);
    JsonResult updatePTask(JSONObject updatePTask);
    JsonResult delPTask(String pid);
}
