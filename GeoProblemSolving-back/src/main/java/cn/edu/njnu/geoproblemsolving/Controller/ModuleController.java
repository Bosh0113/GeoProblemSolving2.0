package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Module.ModuleDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ModuleEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/module")
public class ModuleController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String createModule(@RequestBody ModuleEntity module){
        ModuleDaoImpl moduleDao=new ModuleDaoImpl(mongoTemplate);
        try {
            Date date=new Date();
            SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String moduleId=UUID.randomUUID().toString();
            module.setModuleId(moduleId);
            module.setCreateTime(dateFormat.format(date));
            return moduleDao.createModule(module);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readModule(@RequestParam("key") String key,@RequestParam("value") String value){
        ModuleDaoImpl moduleDao=new ModuleDaoImpl(mongoTemplate);
        try {
            return moduleDao.readModule(key,value);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteModule(@RequestParam("moduleId") String moduleId){
        ModuleDaoImpl moduleDao=new ModuleDaoImpl(mongoTemplate);
        try {
            moduleDao.deleteModule("moduleId",moduleId);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateModule(HttpServletRequest request){
        ModuleDaoImpl moduleDao=new ModuleDaoImpl(mongoTemplate);
        return moduleDao.updateModule(request);
    }
}
