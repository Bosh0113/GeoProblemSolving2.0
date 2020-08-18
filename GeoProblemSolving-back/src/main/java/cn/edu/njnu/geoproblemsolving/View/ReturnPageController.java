package cn.edu.njnu.geoproblemsolving.View;

import cn.edu.njnu.geoproblemsolving.domain.activity.Project;
import cn.edu.njnu.geoproblemsolving.domain.activity.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Controller
public class ReturnPageController {

    private final ProjectService projectService;
    private final StaticPagesBuilder staticPagesBuilder;

    @Autowired
    public ReturnPageController(ProjectService projectService, StaticPagesBuilder staticPagesBuilder) {
        this.projectService = projectService;
        this.staticPagesBuilder = staticPagesBuilder;
    }

    @RequestMapping(value = "/projectDetail/{aid}",method = RequestMethod.GET)
    public String projectPage(@PathVariable String aid) throws IOException {
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = getServicePath();
        File file = new File(servicePath+"/staticPage/project/"+aid+".html");
        if(!file.exists()){//不存在静态文件则生成
            Project project = projectService.findProject(aid);
            if(project != null) {
                staticPagesBuilder.projectDetailPageBuilder(project);
            }
        }
        return "/staticPage/project/"+aid+".html";
    }

    @RequestMapping(value = "/home",method = RequestMethod.GET)
    public String homePage(){
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = getServicePath();
        File file = new File(servicePath+"/staticPage/home.html");
        if(!file.exists()){//不存在静态文件则生成
            staticPagesBuilder.homePageBuilder();
        }
        return "/staticPage/home.html";
    }
    @RequestMapping(value = "/projectList",method = RequestMethod.GET)
    public String projectListPage(){
        //此请求根路径在templates文件夹，考虑隐藏后缀，用此方法请求。
        String servicePath = getServicePath();
        File file = new File(servicePath+"/staticPage/projectList.html");
        if(!file.exists()){//不存在静态文件则生成
            staticPagesBuilder.projectListPageBuilder(projectService.findProjectsByPage(1,18));
        }
        return "/staticPage/projectList.html";
    }

    @Autowired
    Environment environment;
    private String getServicePath(){
        String servicePath;
        if(environment.getProperty("spring.thymeleaf.prefix")==null){
            servicePath = System.getProperty("user.dir")+"/src/main/resources/templates";
        }
        else {
            servicePath = System.getProperty("user.dir")+"/src/main/webapp";
        }
        return servicePath.replaceAll("\\\\","/");
    }
}
