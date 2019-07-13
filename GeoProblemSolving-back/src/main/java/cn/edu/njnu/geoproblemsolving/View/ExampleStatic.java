package cn.edu.njnu.geoproblemsolving.View;

import cn.edu.njnu.geoproblemsolving.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
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

@Component
public class ExampleStatic {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ExampleStatic(MongoTemplate mongoTemplate){this.mongoTemplate = mongoTemplate;}

    public void buildStaticHTML(HttpServletResponse response) throws IOException {
        ServletOutputStream writer = response.getOutputStream();
        try {
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
            resolver.setSuffix(".html");//模板文件后缀
            TemplateEngine templateEngine = new TemplateEngine();
            templateEngine.setTemplateResolver(resolver);

            //构造上下文(Model)
            Context context = new Context();
            UserEntity user = mongoTemplate.findAll(UserEntity.class).get(0);
            context.setVariable("user", user);

            //渲染模板
            String servicePath = System.getProperty("user.dir")+"\\src\\main\\webapp";
            String htmlPath = servicePath+"/example";
            File temp = new File(htmlPath);
            if (!temp.exists()) {
                temp.mkdirs();
            }
            FileWriter write = new FileWriter(htmlPath+"/result.html");
            templateEngine.process("example", context, write);
            writer.print("Success");
        }catch (Exception e){
            writer.print("Fail");
        }finally {
            writer.flush();
            writer.close();
        }
    }
}
