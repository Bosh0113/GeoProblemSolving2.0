package cn.edu.njnu.geoproblemsolving.business.tool;

import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.AddToolEntityDTO;
import cn.edu.njnu.geoproblemsolving.business.tool.support.dto.UpdateToolEntityDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/tool")
public class ToolController {

    @Autowired
    ToolService toolService;

    @Autowired
    MongoTemplate mongoTemplate;

    @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public JsonResult createTool(@RequestBody AddToolEntityDTO add){
      return toolService.createTool(add);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonResult deleteTool(@RequestParam("tid") String tid){
        return toolService.deleteByTid(tid);
    }

    @RequestMapping(value = "/update/{tid}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public JsonResult updateTool(@PathVariable("tid") String tid,@RequestBody UpdateToolEntityDTO updateToolEntityDTO){
        return toolService.updateTool(tid,updateToolEntityDTO);
    }

    /**
     * @Deprecated 统一使用资源那边的接口
     * @param request
     * @return
     */
    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(HttpServletRequest request) {
        return toolService.uploadPicture(request);
    }

    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public JsonResult readTool(@RequestParam("key") String key,@RequestParam("value") String value){
        return toolService.readTool(key,value);
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public JsonResult readTools(@RequestBody String[] tools){
        return toolService.readTools(tools);
    }

    @RequestMapping(value = "/findByProvider/{provider}", method = RequestMethod.GET)
    public JsonResult readAllProject(@PathVariable("provider") String provider) {
        return toolService.findAllByProvider(provider);
    }

}
