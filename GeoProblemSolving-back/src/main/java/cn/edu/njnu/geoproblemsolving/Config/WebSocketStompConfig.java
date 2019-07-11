package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketStompConfig{

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer(){
        ServletServerContainerFactoryBean containerFactoryBean=new ServletServerContainerFactoryBean();
        containerFactoryBean.setMaxTextMessageBufferSize(5242800);
        containerFactoryBean.setMaxBinaryMessageBufferSize(5242800);
        return containerFactoryBean;
    }
}
