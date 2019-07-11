package cn.edu.njnu.geoproblemsolving.View;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@CrossOrigin(origins = "*", allowCredentials = "true")
@RequestMapping(value = "/example")
public class ExampleController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/build", method = RequestMethod.GET)
    public void BuildExampleHTML(HttpServletRequest request, HttpServletResponse response){
        ExampleStatic exampleStatic = new ExampleStatic(mongoTemplate);
        try {
            exampleStatic.buildStaticHTML(request,response);
        }catch (Exception ignored){}
    }
}
