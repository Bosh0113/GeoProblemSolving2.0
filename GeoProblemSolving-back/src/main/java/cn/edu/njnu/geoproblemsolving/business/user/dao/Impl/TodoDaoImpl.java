package cn.edu.njnu.geoproblemsolving.business.user.dao.Impl;

import cn.edu.njnu.geoproblemsolving.business.user.dao.TodoDao;
import cn.edu.njnu.geoproblemsolving.business.user.entity.TodoEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class TodoDaoImpl implements TodoDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public JsonResult findByUserId(String userId) {
        try {
            return ResultUtils.success(mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), TodoEntity.class));
        } catch (Exception e) {
            return ResultUtils.error(-2, e.toString());
        }
    }

    @Override
    public JsonResult findById(String ptId) {
        return null;
    }

    @Override
    public JsonResult delById(String ptId) {
        try {
            Query query = new Query(Criteria.where("ptId").is(ptId));
            mongoTemplate.remove(query, TodoEntity.class);
            //只要能显示出来，那肯定 ptId 肯定是存在，那就不用判断，直接返回即可。
            return ResultUtils.success();
        } catch (Exception e) {
            return ResultUtils.error(-2, e.toString());
        }
    }

    @Override
    public JsonResult updateTask(JSONObject taskJson) {
        try {
            Update update = new Update();
            for (Map.Entry entry : taskJson.entrySet()) {
                String key = (String)entry.getKey();
                if (key.equals("ptId") || key.equals("userId")){
                    continue;
                }
                if (key.equals("endTime")){
                    update.set("endTime", entry.getValue());
                }
                //测试一下是否会自动转型
                update.set(key, entry.getValue());
            }
            Query query = new Query(Criteria.where("ptId").is((String) taskJson.get("ptId")));
            TodoEntity one = mongoTemplate.findOne(query, TodoEntity.class);
            mongoTemplate.updateFirst(query, update, TodoEntity.class);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(-2, e.toString());
        }
    }

    @Override
    public JsonResult createTask(TodoEntity personalTask) {
        try {
            return ResultUtils.success(mongoTemplate.save(personalTask));
        }catch (Exception e) {
            return ResultUtils.error(-2, e.toString());
        }
    }
}
