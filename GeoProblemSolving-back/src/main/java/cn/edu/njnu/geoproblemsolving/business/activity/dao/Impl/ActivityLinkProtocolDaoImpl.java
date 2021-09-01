package cn.edu.njnu.geoproblemsolving.business.activity.dao.Impl;

import cn.edu.njnu.geoproblemsolving.business.activity.dao.ActivityLinkProtocolDao;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityLinkProtocol;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ActivityLinkProtocolDaoImpl
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/7/26
 **/
@Repository
public class ActivityLinkProtocolDaoImpl implements ActivityLinkProtocolDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public ActivityLinkProtocol saveProtocol(ActivityLinkProtocol protocol) {
        return mongoTemplate.save(protocol);
    }

    @Override
    public ActivityLinkProtocol findProtocolById(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.findOne(query, ActivityLinkProtocol.class);
    }

    @Override
    public UpdateResult updateLinkProtocol(String id, Update update) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.upsert(query, update, ActivityLinkProtocol.class);
    }

    @Override
    public DeleteResult delLinkProtocol(String id) {
        Query query = new Query(Criteria.where("id").is(id));
        return mongoTemplate.remove(query, ActivityLinkProtocol.class);
    }
}
