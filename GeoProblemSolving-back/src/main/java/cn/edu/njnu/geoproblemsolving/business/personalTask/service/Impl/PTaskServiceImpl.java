package cn.edu.njnu.geoproblemsolving.business.personalTask.service.Impl;

import cn.edu.njnu.geoproblemsolving.business.personalTask.dao.Impl.PTaskDaoImpl;
import cn.edu.njnu.geoproblemsolving.business.personalTask.entity.PersonalTaskEntity;
import cn.edu.njnu.geoproblemsolving.business.personalTask.service.PTaskService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
public class PTaskServiceImpl implements PTaskService {
    @Autowired
    PTaskDaoImpl pTaskDao;

    @Override
    public JsonResult findPersonalTask(String userId) {
        return pTaskDao.findByUserId(userId);
    }

    @Override
    public JsonResult createPersonalTask(PersonalTaskEntity personalTaskEntity) {
        personalTaskEntity.setPtId(UUID.randomUUID().toString());
        String startTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        personalTaskEntity.setStartTime(startTime);


        return pTaskDao.createTask(personalTaskEntity);
    }

    @Override
    public JsonResult updatePTask(JSONObject jsonTask) {
        return pTaskDao.updateTask(jsonTask);
    }

    @Override
    public JsonResult delPTask(String pid) {
        return pTaskDao.delById(pid);
    }
}
