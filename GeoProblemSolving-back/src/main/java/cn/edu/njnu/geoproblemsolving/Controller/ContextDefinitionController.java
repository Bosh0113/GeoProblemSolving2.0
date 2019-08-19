package cn.edu.njnu.geoproblemsolving.Controller;
import cn.edu.njnu.geoproblemsolving.Dao.ContextDefinition.ContextDefinitionDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ContextDefinitionEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/contextdefinition")
public class ContextDefinitionController {
    @Resource
    private MongoTemplate mongoTemplate;

//    创建页面，带subprojectid
    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String createContextDefinition(@RequestBody ContextDefinitionEntity contextDefinition){
        ContextDefinitionDaoImpl contextDefinitionDao = new ContextDefinitionDaoImpl(mongoTemplate);
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String stepId=UUID.randomUUID().toString();
            contextDefinition.setStepId(stepId);
            contextDefinition.setCreateTime(dateFormat.format(date));
//            contextDefinition.setSubProjectId(subProjectId);

            return contextDefinitionDao.createStep(contextDefinition);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readContextDefinition(@RequestParam("key") String key,@RequestParam("value") String value){
        ContextDefinitionDaoImpl contextDefinitionDao = new ContextDefinitionDaoImpl(mongoTemplate);
        try {
            return contextDefinitionDao.readContextDefinition(key,value);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteContextDefinition(@RequestParam("ContextDefinitionId") String contextDefinitionId){
        ContextDefinitionDaoImpl contextDefinitionDao = new ContextDefinitionDaoImpl(mongoTemplate);
        try {
            contextDefinitionDao.deleteContextDefinition("ContextDefinitionId",contextDefinitionId);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateModule(HttpServletRequest request){
        ContextDefinitionDaoImpl contextDefinitionDao = new ContextDefinitionDaoImpl(mongoTemplate);
        return contextDefinitionDao.updateContextDefinition(request);
    }
}
