package cn.edu.njnu.geoproblemsolving.View;

import cn.edu.njnu.geoproblemsolving.Entity.ProjectEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileWriter;

@Component
public class StaticPagesBuilder {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public StaticPagesBuilder(MongoTemplate mongoTemplate){this.mongoTemplate=mongoTemplate;}

    public void projectDetailPageBuilder(String projectId){
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
            resolver.setSuffix(".html");//模板文件后缀
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            Context context = new Context();
            Query queryProjectInfo = new Query(Criteria.where("projectId").is(projectId));
            ProjectEntity projectEntity = mongoTemplate.findOne(queryProjectInfo,ProjectEntity.class);
            context.setVariable("projectInfo",projectEntity);
            context.setVariable("activeName","projects");

            //渲染模板
//            String servicePath = System.getProperty("user.dir")+"/src/main/webapp";
            String servicePath = System.getProperty("user.dir")+"/src/main/resources/templates";
            String htmlPath = servicePath+"/project";
            File temp = new File(htmlPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            String htmlFile = htmlPath+"/"+projectId+".html";
            try{
                FileWriter write = new FileWriter(htmlFile);
                templateEngine.process("projectDetail", context, write);
                write.flush();
                write.close();
            }catch (Exception ignored){}
    }
}
