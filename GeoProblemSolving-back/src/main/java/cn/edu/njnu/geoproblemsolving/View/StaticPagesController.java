package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/static")
public class StaticPagesController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/projectDetail",method = RequestMethod.GET)
    public void projectDetail(@RequestParam("projectId") String projectId){
        StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
        staticPagesBuilder.projectDetailPageBuilder(projectId);
    }

    @RequestMapping(value = "/project/create",method = RequestMethod.GET)
    public void projectList(){
        StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
        staticPagesBuilder.projectListPageBuilder();
    }
    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public void home(){
        StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
        staticPagesBuilder.homePageBuilder();
    }
}