package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns(
                        "/user",
                        "/user/changePwd/**",
                        "/user/avatar",
                        "/user/todo/**",
                        "/tool/**",
                        "/toolset/**",
                        "/chat/**",
                        "/integration/**",
                        "/rip/**",
                        "/res/**",
                        "/modelTask/**",
                        "/dataContainer/**",
                        "/msgRecords/**",
                        "/activity/**",
                        "/activityDoc/**",
                        "/activityDriven/**",
                        "/subproject/**",
                        "/task/**",
                        "/notice/**"
                )//拦截的请求
                .excludePathPatterns(
                        "/rip/file/allPublic"
                );//不拦截的请求
    }
    /*
    project 不用拦截，projectList 页面已经能读取到 project 相关信息
    activity 是下层的肯定要登陆才能获取
     */
}
