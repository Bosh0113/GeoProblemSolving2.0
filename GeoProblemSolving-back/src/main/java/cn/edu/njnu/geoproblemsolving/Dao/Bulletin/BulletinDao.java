package cn.edu.njnu.geoproblemsolving.Dao.Bulletin;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.BulletinEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class BulletinDao implements IBulletinDaoImpl {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public BulletinDao(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Object saveBulletin(BulletinEntity bulletinEntity) {
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            bulletinEntity.setBulletinId(UUID.randomUUID().toString());
            bulletinEntity.setTime(dateFormat.format(date));
            mongoTemplate.save(bulletinEntity);
            return bulletinEntity;
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object inquiryBulletin(String key, String value) {
        try {
            Query query = Query.query(Criteria.where(key).is(value));
            return mongoTemplate.find(query, BulletinEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public Object updateBulletin(HttpServletRequest request) {
        try {
            Query query = Query.query(Criteria.where("bulletinId").is(request.getParameter("bulletinId")));
            CommonMethod method = new CommonMethod();
            Update update = method.setUpdate(request);
            mongoTemplate.updateFirst(query, update, BulletinEntity.class);
            return mongoTemplate.findOne(query, BulletinEntity.class);
        } catch (Exception e) {
            return "Fail";
        }
    }

    @Override
    public String deleteBulletin(String bulletinId) {
        try {
            Query query = Query.query(Criteria.where("bulletinId").is(bulletinId));
            mongoTemplate.remove(query, BulletinEntity.class);
            return "Success";
        } catch (Exception e) {
            return "Fail";
        }
    }
}
