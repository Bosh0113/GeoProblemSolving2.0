package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre){
        HttpSession session = ((HttpServletRequest) sre.getServletRequest()).getSession();
    }
    public RequestListener() {}
    @Override
    public void requestDestroyed(ServletRequestEvent arg0)  {}
}
