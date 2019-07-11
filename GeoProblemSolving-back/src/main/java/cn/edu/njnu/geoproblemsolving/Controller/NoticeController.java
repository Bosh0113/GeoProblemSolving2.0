package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Notice.NoticeDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.NoticeEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
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
