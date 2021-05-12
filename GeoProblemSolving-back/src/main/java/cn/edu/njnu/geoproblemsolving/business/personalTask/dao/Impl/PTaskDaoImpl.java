package cn.edu.njnu.geoproblemsolving.business.personalTask.dao.Impl;

import cn.edu.njnu.geoproblemsolving.business.personalTask.dao.PTaskDao;
import cn.edu.njnu.geoproblemsolving.business.personalTask.entity.PersonalTaskEntity;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.result.DeleteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class PTaskDaoImpl implements PTaskDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public JsonResult findByUserId(String userId) {
        try {
            return ResultUtils.success(mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), PersonalTaskEntity.class));
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
            mongoTemplate.remove(query, PersonalTaskEntity.class);
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
            PersonalTaskEntity one = mongoTemplate.findOne(query, PersonalTaskEntity.class);
            mongoTemplate.updateFirst(query, update, PersonalTaskEntity.class);
            return ResultUtils.success();
        }catch (Exception e){
            return ResultUtils.error(-2, e.toString());
        }
    }

    @Override
    public JsonResult createTask(PersonalTaskEntity personalTask) {
        try {
            return ResultUtils.success(mongoTemplate.save(personalTask));
        }catch (Exception e) {
            return ResultUtils.error(-2, e.toString());
        }
    }
}
