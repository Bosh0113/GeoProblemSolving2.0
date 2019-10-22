package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Tool_related.ToolsetDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/toolset")
public class ToolsetController {

    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String createToolset(@RequestBody ToolsetEntity toolset){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        try {
            return toolsetDao.createToolset(toolset);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readToolset(@RequestParam("key") String key, @RequestParam("value") String value){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        try {
            return toolsetDao.readToolset(key,value);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteToolset(@RequestParam("tsid") String tsid){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        try {
            toolsetDao.deleteToolset("tsid",tsid);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public String uploadPicture(HttpServletRequest request) {
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        return toolsetDao.uploadPicture(request);
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateToolset(HttpServletRequest request){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        return toolsetDao.updateToolset(request);
    }
}
