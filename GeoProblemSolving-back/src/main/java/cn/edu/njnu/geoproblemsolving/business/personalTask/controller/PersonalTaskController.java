package cn.edu.njnu.geoproblemsolving.business.personalTask.controller;

import cn.edu.njnu.geoproblemsolving.business.personalTask.entity.PersonalTaskEntity;
import cn.edu.njnu.geoproblemsolving.business.personalTask.service.Impl.PTaskServiceImpl;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pTask")
public class PersonalTaskController {
    @Autowired
    PTaskServiceImpl pTaskService;

    /**
     * 创建 personal Task
     * @param personalTask
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public JsonResult createTask(@RequestBody PersonalTaskEntity personalTask){
        return pTaskService.createPersonalTask(personalTask);
    }

    /**
     * 删除
     * @param ptId
     * @return
     */
    @RequestMapping(value = "/{ptId}", method = RequestMethod.DELETE)
    public JsonResult delTask(@PathVariable String ptId){
        return pTaskService.delPTask(ptId);
    }

    /**
     *修改
     * @param jsonTask
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public JsonResult updateTask(@RequestBody JSONObject jsonTask){
        return pTaskService.updatePTask(jsonTask);
    }

    /**
     * 使用userId 查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public JsonResult getUserTask(@PathVariable String userId){
        return pTaskService.findPersonalTask(userId);
    }
}
