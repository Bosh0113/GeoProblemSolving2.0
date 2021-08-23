package cn.edu.njnu.geoproblemsolving.View;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.Project;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.*;
import java.util.List;

@Component
public class StaticPagesBuilder {

    public void projectDetailPageBuilder(Project project) throws IOException {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");   //模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");        //模板文件后缀
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariable("projectInfo", project);
        context.setVariable("activeName", "projects");

        //渲染模板
        String servicePath = getServicePath();
        String htmlPath = servicePath + "/staticPage/project";
        File temp = new File(htmlPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        String htmlFile = htmlPath + "/" + project.getAid() + ".html";
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8");
            templateEngine.process("projectDetail", context, write);
            write.flush();
            write.close();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void projectListPageBuilder(JSONObject projects) {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        context.setVariable("projectsInfo", projects);

        //渲染模板
        String servicePath = getServicePath();
        String htmlPath = servicePath + "/staticPage";
        File temp = new File(htmlPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        String htmlFile = htmlPath + "/projectList.html";
        try {
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8");
            templateEngine.process("projectList", context, write);
            write.flush();
            write.close();
        } catch (Exception ignored) {
        }
    }

    public void homePageBuilder() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        String servicePath = getServicePath();
        resolver.setPrefix("templates/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        Context context = new Context();
        String htmlPath = servicePath + "/staticPage";
        File temp = new File(htmlPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        String htmlFile = htmlPath + "/home.html";
        try {
            FileWriter write = new FileWriter(htmlFile);
            templateEngine.process("home", context, write);//优化点，写加锁
            write.flush();
            write.close();
        } catch (Exception ignored) {
        }
    }

    public void projectDetailPageRemove(String aid) {
        // 静态页面路径
        String htmlPath = getServicePath() + "/staticPage/project";
        File temp = new File(htmlPath);
        if (!temp.exists()) {
            temp.mkdirs();
        }
        String htmlFile = htmlPath + "/" + aid + ".html";
        // 删除
        File file = new File(htmlFile);
        if (file.isFile()) {
            file.delete();
        }
    }

    private String getServicePath() {
        String servicePath = System.getProperty("user.dir") + "/src/main/resources/templates";
       // String servicePath = System.getProperty("user.dir")+"/src/main/webapp"; //部署使用
        return servicePath.replaceAll("\\\\", "/");
    }
}




