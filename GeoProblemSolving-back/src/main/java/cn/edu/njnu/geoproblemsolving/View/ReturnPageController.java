package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.File;

@Controller
public class ReturnPageController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/projectDetail/{projectId}",method = RequestMethod.GET)
    public String projectPage(@PathVariable String projectId){
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = System.getProperty("user.dir")+"/src/main/resources/templates";
        File file = new File(servicePath+"/staticPage/project/"+projectId+".html");
        if(!file.exists()){//不存在静态文件则生成
            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
            staticPagesBuilder.projectDetailPageBuilder(projectId);
        }
        return "/staticPage/project/"+projectId+".html";
    }

    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public String homePage(){
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = System.getProperty("user.dir")+"/src/main/resources/templates";
        File file = new File(servicePath+"/staticPage/home.html");
        if(!file.exists()){//不存在静态文件则生成
            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
            staticPagesBuilder.homePageBuilder();
        }
        return "/staticPage/home.html";
    }
    @RequestMapping(value = "/projectlist",method = RequestMethod.GET)
    public String projectListPage(){
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = System.getProperty("user.dir")+"/src/main/resources/templates";
        File file = new File(servicePath+"/staticPage/projectList.html");
        if(!file.exists()){//不存在静态文件则生成
            StaticPagesBuilder staticPagesBuilder = new StaticPagesBuilder(mongoTemplate);
            staticPagesBuilder.projectListPageBuilder();
        }
        return "/staticPage/projectList.html";
    }
}
