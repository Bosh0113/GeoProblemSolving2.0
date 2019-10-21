package cn.edu.njnu.geoproblemsolving.Controller;

import cn.edu.njnu.geoproblemsolving.Dao.Tool_related.ToolDaoImpl;
import cn.edu.njnu.geoproblemsolving.Entity.ToolEntity;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/tool")
public class ToolController {
    @Resource
    private MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public String createTool(@RequestBody ToolEntity tool){
        ToolDaoImpl toolDao = new ToolDaoImpl(mongoTemplate);
        try {
            return toolDao.createTool(tool);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public Object readTool(@RequestParam("key") String key,@RequestParam("value") String value){
        ToolDaoImpl toolDao = new ToolDaoImpl(mongoTemplate);
        try {
            return toolDao.readTool(key,value);
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String deleteTool(@RequestParam("tid") String tid){
        ToolDaoImpl toolDao = new ToolDaoImpl(mongoTemplate);
        try {
            toolDao.deleteTool("tid",tid);
            return "Success";
        }catch (Exception e){
            return "Fail";
        }
    }

    @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public String updateTool(HttpServletRequest request){
        ToolDaoImpl toolDao = new ToolDaoImpl(mongoTemplate);
        return toolDao.updateTool(request);
    }
}
