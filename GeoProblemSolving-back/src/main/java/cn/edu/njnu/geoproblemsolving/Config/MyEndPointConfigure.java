package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class MyEndPointConfigure extends ServerEndpointConfig.Configurator implements ApplicationContextAware {

    private static BeanFactory context;

    @Override
    public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException
    {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("auto load"+this.hashCode());
        MyEndPointConfigure.context = applicationContext;
    }

    @Override
    public void modifyHandshake(ServerEndpointConfig config,
                                HandshakeRequest request,
                                HandshakeResponse response)
    {
        HttpSession httpSession = (HttpSession)request.getHttpSession();
        if (httpSession!=null){
            System.out.println("获得到userId："+httpSession.getAttribute("userId"));
            config.getUserProperties().put(HttpSession.class.getName(),httpSession);
        }
    }
}
