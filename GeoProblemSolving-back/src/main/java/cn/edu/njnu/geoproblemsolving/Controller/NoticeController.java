package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Notice.NoticeDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.NoticeEntity;
import cn.edu.njnu.geoproblemsolving.business.user.dao.IUserImpl;
import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save",produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String saveNotice(@RequestBody NoticeEntity notice){
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.saveNotice(notice);
    }

    @RequestMapping(value = "/send", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String sendNotice(@RequestBody JSONObject noticeJson){
        String recipientEmail = noticeJson.getString("recipient");
        JSONObject content = noticeJson.getJSONObject("content");
        IUserImpl userDao = new IUserImpl();
        User noticedUser = mongoTemplate.findOne(new Query(Criteria.where("email").is(recipientEmail)), User.class);
        String recipientId = noticedUser.getUserId();
        NoticeEntity noticeEntity = new NoticeEntity();
        noticeEntity.setRecipientId(recipientId);
        noticeEntity.setContent(content);
        noticeEntity.setState("unread");
        noticeEntity.setType("notice");
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.saveNotice(noticeEntity);
    }

    @RequestMapping(value = "/inquiry",method = RequestMethod.GET)
    public Object inquiryNotice(@RequestParam("key") String key,@RequestParam("value") String value){
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.inquiryNotice(key, value);
    }

    @RequestMapping(value = "/read",method = RequestMethod.GET)
    public String readNotice(@RequestParam("noticeId") String noticeId){
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.updateRead(noticeId);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateNotice(HttpServletRequest request){
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.updateNotice(request);
    }

    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public String deleteNotice(@RequestParam("noticeId") String noticeId){
        NoticeDaoImpl noticeDao=new NoticeDaoImpl(mongoTemplate);
        return noticeDao.deleteNotice(noticeId);
    }
}
