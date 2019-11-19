package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Tool_related.ToolsetDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;
import cn.edu.njnu.geoproblemsolving.Entity.ToolReq.AddToolReq;
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
    public Object createToolset(@RequestBody ToolsetEntity toolset){
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

    @RequestMapping(value = "/inquiryAll", method = RequestMethod.GET)
    public Object readAllProject(@RequestParam("provider") String provider) {
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        return toolsetDao.readAccessibleToolsets(provider);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteToolset(@RequestParam("tsId") String tsId){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        try {
            toolsetDao.deleteToolset("tsId",tsId);
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


    @RequestMapping(value = "/addTool", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String addTool(@RequestBody AddToolReq addToolReq){
        ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
        return toolsetDao.addToolToToolset(addToolReq.getNewTool(),addToolReq.getTsIds());
    }
}
