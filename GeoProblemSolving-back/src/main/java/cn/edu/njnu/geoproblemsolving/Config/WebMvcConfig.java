package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns(
//                        "/subProject/inquiry",
//                        "/subProject/create",
//                        "/subProject/delete",
//                        "/subProject/update",
//                        "/subProject/manager",
//                        "/module/create",
//                        "/module/inquiry",
//                        "/resource/upload",
//                        "/project/create",
//                        "/project/delete",
//                        "/project/update",
//                        "/project/quit",
//                        "/project/manager",
//                        "/task/save",
//                        "/task/update",
//                        "/notice/read",
//                        "/notice/update",
//                        "/notice/delete",
//                        "/bulletin/save",
//                        "/bulletin/update",
//                        "/bulletin/delete",
//                        "/history/inquiry"
                )//拦截的请求
                .excludePathPatterns();//不拦截的请求
    }
}
