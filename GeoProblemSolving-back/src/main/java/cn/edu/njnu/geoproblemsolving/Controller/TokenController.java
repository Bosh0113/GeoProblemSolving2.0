package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.ShareToken.ShareTokenDaoImpl;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/token")
public class TokenController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/getShareToken", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public String getShareToken(@RequestParam("duration") long ttlMillis){
        ShareTokenDaoImpl shareTokenDao = new ShareTokenDaoImpl(mongoTemplate);
        return shareTokenDao.getShareToken(ttlMillis);
    }

    @RequestMapping(value = "/checkShareToken", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.GET)
    public Boolean checkShareToken(@RequestParam("shareToken") String shareToken){
        ShareTokenDaoImpl shareTokenDao = new ShareTokenDaoImpl(mongoTemplate);
        return shareTokenDao.checkShareToken(shareToken);
    }
}
