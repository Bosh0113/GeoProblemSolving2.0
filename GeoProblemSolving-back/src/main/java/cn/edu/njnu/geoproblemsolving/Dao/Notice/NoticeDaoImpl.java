package cn.edu.njnu.geoproblemsolving.Dao.Notice;

import cn.edu.njnu.geoproblemsolving.Dao.Method.CommonMethod;
import cn.edu.njnu.geoproblemsolving.Entity.NoticeEntity;
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
public class NoticeDaoImpl implements INoticeDao{

    private final MongoTemplate mongoTemplate;

    @Autowired
    public NoticeDaoImpl(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    @Override
    public String saveNotice(NoticeEntity notice){
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            notice.setNoticeId(UUID.randomUUID().toString());
            notice.setState("unread");
            notice.setCreatedTime(dateFormat.format(date));
            mongoTemplate.save(notice);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public Object inquiryNotice(String key,String value){
        try {
            Query query=new Query(Criteria.where(key).is(value));
            return mongoTemplate.find(query,NoticeEntity.class);
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String deleteNotice(String noticeId){
        try {
            Query query=new Query(Criteria.where("noticeId").is(noticeId));
            mongoTemplate.remove(query,NoticeEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateRead(String noticeId){
        try {
            Query query=new Query(Criteria.where("noticeId").is(noticeId));
            Update update=new Update();
            update.set("state","read");
            mongoTemplate.updateFirst(query,update,NoticeEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @Override
    public String updateNotice(HttpServletRequest request){
        try {
            Query query=new Query(Criteria.where("noticeId").is(request.getParameter("noticeId")));
            CommonMethod method=new CommonMethod();
            Update update=method.setUpdate(request);
            mongoTemplate.updateFirst(query,update,NoticeEntity.class);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }
}
