package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.MultipartConfigElement;

@Component
public class FileUploadConfig {

    @Bean
    public MultipartResolver custom(){
        return new CommonsMultipartResolver();
    }

    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory= new MultipartConfigFactory();
        factory.setMaxFileSize("1024MB");
        factory.setMaxRequestSize("102400MB");
//        factory.setLocation("/");
        return factory.createMultipartConfig();
    }
}
