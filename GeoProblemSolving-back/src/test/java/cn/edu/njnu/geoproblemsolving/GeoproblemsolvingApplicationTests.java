package cn.edu.njnu.geoproblemsolving;


import cn.edu.njnu.geoproblemsolving.business.user.entity.User;
import com.alibaba.fastjson.JSONObject;
import org.atteo.evo.inflector.English;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GeoproblemsolvingApplicationTests {

    @Test
    public void contextLoads() {
    }

}
