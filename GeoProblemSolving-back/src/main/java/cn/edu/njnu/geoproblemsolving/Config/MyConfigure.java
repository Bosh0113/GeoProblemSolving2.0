package cn.edu.njnu.geoproblemsolving.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyConfigure {

    @Bean
    public MyEndPointConfigure newConfigure()
    {
        return new MyEndPointConfigure();
    }
}
