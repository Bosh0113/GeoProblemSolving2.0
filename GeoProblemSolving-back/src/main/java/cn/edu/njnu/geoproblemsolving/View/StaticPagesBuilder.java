package cn.edu.njnu.geoproblemsolving.View;

import cn.edu.njnu.geoproblemsolving.Entity.HistoryEventEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ResourceEntity;
import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class StaticPagesBuilder {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StaticPagesBuilder(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    public void personalPageBuilder(String userId){
        try {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
            resolver.setSuffix(".html");//模板文件后缀
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            Context context = new Context();
            Query queryUser = new Query(Criteria.where("userId").is(userId));
            UserEntity userEntity = mongoTemplate.findOne(queryUser,UserEntity.class);
            context.setVariable("userInfo",userEntity);

            Query queryResource = new Query(Criteria.where("uploaderId").is(userId));
            List<ResourceEntity> resourceEntities = mongoTemplate.find(queryResource,ResourceEntity.class);
            context.setVariable("userResourceList",resourceEntities);

            Query queryManageProjects = new Query(Criteria.where("managerId").is(userId));
            List<ProjectEntity> manageProjects = mongoTemplate.find(queryManageProjects,ProjectEntity.class);
            context.setVariable("userManagerProjectList",manageProjects);

            ArrayList<ProjectEntity> joinedProjects = new ArrayList<>();
            assert userEntity != null;
            JSONArray joinedList = userEntity.getJoinedProjects();
            for (int i=0;i<joinedList.size();i++){
                JSONObject joinedProjectInfo = joinedList.getJSONObject(i);
                Query query = new Query(Criteria.where("projectId").is(joinedProjectInfo.getString("projectId")));
                ProjectEntity projectEntity = mongoTemplate.findOne(query,ProjectEntity.class);
                joinedProjects.add(projectEntity);
            }
            context.setVariable("joinedProjectsList",joinedProjects);

            Query queryEvent=new Query(Criteria.where("eventType").is("project").and("userId").is(userId));
            List<HistoryEventEntity> eventEntities = mongoTemplate.find(queryEvent,HistoryEventEntity.class);
            context.setVariable("userEventList",eventEntities);

            //渲染模板
            String servicePath = System.getProperty("user.dir")+"\\src\\main\\webapp";
            String htmlPath = servicePath+"\\personal";
            File temp = new File(htmlPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String htmlFile = htmlPath+"\\"+userId+".html";
            System.out.println("Build File: "+htmlFile);
            FileWriter write = new FileWriter(htmlFile);
            templateEngine.process("personalPage", context, write);
            System.out.println("personal page has been build success.");
        }catch (Exception ignored){}
    }

    public void projectListBuilder() {
        try {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
            resolver.setSuffix(".html");//模板文件后缀
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            //构造上下文(Model)
            Context context = new Context();
            List<ProjectEntity> projects = mongoTemplate.findAll(ProjectEntity.class);
            List<UserEntity> users = mongoTemplate.findAll(UserEntity.class);
            context.setVariable("projects", projects);
//            context.setVariable("users",users);

//            context.setVariable("");

            //渲染模板
            String servicePath =System.getProperty("user.dir")+"\\src\\main\\webapp";
            String htmlPath = servicePath+"/Projects";
            File temp = new File(htmlPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }

            String htmlFile = htmlPath+"/result.html";
            System.out.println("Build File: "+htmlFile);
            FileWriter write = new FileWriter(htmlFile);
            templateEngine.process("projectList", context, write);
            System.out.println("projectlist page has been build success.");
        }catch (Exception ignored){}

    }
}
