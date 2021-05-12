package cn.edu.njnu.geoproblemsolving.business.personalTask.service;

import cn.edu.njnu.geoproblemsolving.business.personalTask.entity.PersonalTaskEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;

public interface PTaskService {
    JsonResult findPersonalTask(String userId);
    JsonResult createPersonalTask(PersonalTaskEntity personalTaskEntity);
    JsonResult updatePTask(JSONObject updatePTask);
    JsonResult delPTask(String pid);
}
