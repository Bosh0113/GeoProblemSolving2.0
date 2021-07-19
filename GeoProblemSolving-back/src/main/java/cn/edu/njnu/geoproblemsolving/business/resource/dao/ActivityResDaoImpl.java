package cn.edu.njnu.geoproblemsolving.business.resource.dao;

import cn.edu.njnu.geoproblemsolving.business.resource.entity.ResourceEntity;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @ClassName ResourceDaoImpl
 * @Description 项目中资源相关相关操作
 * @Author zhngzhng
 * @Date 2021/4/20
 **/
@Repository
public class ActivityResDaoImpl implements ActivityResDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ResourceEntity addResource(ResourceEntity res) {
        return mongoTemplate.save(res);
    }

    @Override
    public Long delResource(String uid) {
        Query query = new Query(Criteria.where("uid").is(uid));
        long deleteResult = mongoTemplate.remove(query, ResourceEntity.class).getDeletedCount();
        return deleteResult;
    }

    @Override
    public List<ResourceEntity> queryByAid(String aid) {
        Query query = new Query(Criteria.where("activityId").is(aid));
        List<ResourceEntity> resourceEntityList = mongoTemplate.find(query, ResourceEntity.class);
        return resourceEntityList;
    }

    @Override
    public String updateRes(Query query, Update update) {
        UpdateResult upsert = mongoTemplate.upsert(query, update, ResourceEntity.class);
        if (upsert.getModifiedCount() == 1){
            return "suc";
        }else {
            return "fail";
        }
    }

    @Override
    public ResourceEntity queryByUid(String uid) {
        Query query = new Query(Criteria.where("uid").is(uid));
        return mongoTemplate.findOne(query, ResourceEntity.class);
    }
}
