package cn.edu.njnu.geoproblemsolving.business.user.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.user.dao.Impl.TodoDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.TodoEntity;
import cn.edu.njnu.geoproblemsolving.business.user.service.TodoService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class TodoServiceImpl implements TodoService {
    @Autowired
    TodoDaoImpl todoDao;

    @Override
    public JsonResult findPersonalTask(String userId) {
        return todoDao.findByUserId(userId);
    }

    @Override
    public JsonResult createPersonalTask(TodoEntity todoEntity) {
        todoEntity.setPtId(UUID.randomUUID().toString());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        todoEntity.setStartTime(startTime);
        return todoDao.createTask(todoEntity);
    }

    @Override
    public JsonResult updatePTask(JSONObject jsonTask) {
        return todoDao.updateTask(jsonTask);
    }

    @Override
    public JsonResult delPTask(String pid) {
        return todoDao.delById(pid);
    }
}
