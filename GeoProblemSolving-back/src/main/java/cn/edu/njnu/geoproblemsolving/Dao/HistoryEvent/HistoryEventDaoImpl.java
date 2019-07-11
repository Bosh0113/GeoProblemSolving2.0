package cn.edu.njnu.geoproblemsolving.Dao.HistoryEvent;

import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
public class HistoryEventDaoImpl implements IHistoryEventDao {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public HistoryEventDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveHistoryEvent(HistoryEventEntity historyEvent){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            historyEvent.setCreateTime(dateFormat.format(date));
            historyEvent.setHistoryId(UUID.randomUUID().toString());
            mongoTemplate.save(historyEvent);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryHistoryEvent(String eventType, String key, String value){
        try {

            Query query=new Query(Criteria.where("eventType").is(eventType).and(key).is(value));
            if (mongoTemplate.find(query,HistoryEventEntity.class).isEmpty()){
                return "None";
            }
            else {
                return mongoTemplate.find(query,HistoryEventEntity.class);
            }

        }catch (Exception e){
            return "Fail";
        }
    }
}
