package cn.edu.njnu.geoproblemsolving.Config;

import cn.edu.njnu.geoproblemsolving.business.StaticParams;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class MyInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) throws IOException {
        HttpSession session = request.getSession();
        // session.getAttribute("userId")==null
        if(StaticParams.loginUser == null){
            ServletOutputStream writer = response.getOutputStream();
            writer.print("Offline");
            writer.flush();
            writer.close();
            return false;
        }
        return true;
    }
}
