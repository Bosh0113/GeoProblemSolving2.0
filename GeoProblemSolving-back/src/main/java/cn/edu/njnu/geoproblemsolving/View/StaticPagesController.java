package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/static")
public class StaticPagesController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/personalPage", method = RequestMethod.GET)
    public void personalPage(@RequestParam("userId") String userId){
        StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
        staticPagesBuilder.personalPageBuilder(userId);
    }
}
