package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Bulletin.BulletinDao;
import cn.edu.njnu.geoproblemsolving.Entity.BulletinEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/bulletin")
public class BulletinController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/save", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object saveBulletin(@RequestBody BulletinEntity bulletinEntity) {
        BulletinDao bulletinDao = new BulletinDao(mongoTemplate);
        return bulletinDao.saveBulletin(bulletinEntity);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object inquiryBulletin(@RequestParam("key") String key, @RequestParam("value") String value) {
        BulletinDao bulletinDao = new BulletinDao(mongoTemplate);
        return bulletinDao.inquiryBulletin(key, value);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Object updateBulletin(HttpServletRequest request) {
        BulletinDao bulletinDao = new BulletinDao(mongoTemplate);
        return bulletinDao.updateBulletin(request);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteBulletin(@RequestParam("bulletinId") String bulletinId) {
        BulletinDao bulletinDao = new BulletinDao(mongoTemplate);
        return bulletinDao.deleteBulletin(bulletinId);
    }
}
