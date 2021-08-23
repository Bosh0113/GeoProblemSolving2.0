package cn.edu.njnu.geoproblemsolving.business.activity.dao.Impl;

import cn.edu.njnu.geoproblemsolving.business.activity.dao.LinkProtocolDao;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

/**
 * @ClassName LinkProtocolDaoImpl
 * @Description Todo
 * @Author zhngzhng
 * @Date 2021/7/21
 **/
@Component
public class LinkProtocolDaoImpl implements LinkProtocolDao {
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public LinkProtocol setProtocol(LinkProtocol protocol) {
        //局部变量，存储于栈中
        protocol.setPid(UUID.randomUUID().toString());
        return mongoTemplate.save(protocol);
    }

    @Override
    public LinkProtocol findProtocolById(String id) {
        Query query = new Query(Criteria.where("pid").is(id));
        return mongoTemplate.findOne(query, LinkProtocol.class);
    }

    @Override
    public UpdateResult updateLinkProtocol(String id, Update update) {
        Query query = new Query(Criteria.where("pid").is(id));
        return mongoTemplate.upsert(query, update, LinkProtocol.class);
    }

    @Override
    public DeleteResult delLinkProtocol(String id) {
        Query query = new Query(Criteria.where("pid").is(id));
        return mongoTemplate.remove(query, LinkProtocol.class);
    }
}
