package cn.edu.njnu.geoproblemsolving.Config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: David.Xiao
 * @Date: 2019/4/14 15:18
 * @Description:
 */
@Configuration
public class HttpConfig {

    //http端口
    @Value("${http.port}")
    private int httpPort;

    //https端口,8083
    @Value("${server.port}")
    Integer httpsPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector connector =new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(httpPort);
        tomcat.addAdditionalTomcatConnectors(connector); // 添加http
        connector.setRedirectPort(httpsPort);
        return tomcat;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HttpConfig.class, args);
    }


}
