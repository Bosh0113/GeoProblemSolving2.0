package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.data.mongodb.core.MongoTemplate;
<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
=======
import org.springframework.web.bind.annotation.*;
>>>>>>> master

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/static")
public class StaticPagesController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/personalPage", method = RequestMethod.GET)
    public void personalPage(@RequestParam("userId") String userId){
        StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
        staticPagesBuilder.personalPageBuilder(userId);
    }

<<<<<<< HEAD

    @RequestMapping(value = "/project/create", method = RequestMethod.GET)
    public void BuildExampleHTML(){
        StaticPagesBuilder projectStatic = new StaticPagesBuilder(mongoTemplate);
        try {
            projectStatic.projectListBuilder();
        }catch (Exception ignored){}
=======
    @RequestMapping(value = "/projectDetail",method = RequestMethod.GET)
    public void projectDetail(@RequestParam("projectId") String projectId){
            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
            staticPagesBuilder.projectDetailPageBuilder(projectId);
>>>>>>> master
    }
}
