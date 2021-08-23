package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns(
                        "/**"
                )//拦截的请求
                .excludePathPatterns("/user/**")
                .excludePathPatterns("/**/*.jpeg")
                .excludePathPatterns("/**/notice/**/");//不拦截的请求
    }
}
