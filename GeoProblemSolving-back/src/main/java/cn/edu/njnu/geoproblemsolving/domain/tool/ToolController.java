package cn.edu.njnu.geoproblemsolving.domain.tool;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.JsonResult;
import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import cn.edu.njnu.geoproblemsolving.domain.tool.dto.AddToolEntityDTO;
import cn.edu.njnu.geoproblemsolving.domain.tool.dto.UpdateToolEntityDTO;
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
      return ResultUtils.success(toolService.createTool(add));
    }


    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public JsonResult deleteTool(@RequestParam("tId") String tid){
        toolService.deleteByTId(tid);
        return ResultUtils.success();
    }

    @RequestMapping(value = "/update/{tId}", produces = {"application/json;charset=UTF-8"},method = RequestMethod.POST)
    public JsonResult updateTool(@PathVariable("tId") String tId,@RequestBody UpdateToolEntityDTO updateToolEntityDTO){
        return ResultUtils.success(toolService.updateTool(tId,updateToolEntityDTO));
    }

    @RequestMapping(value = "/picture", method = RequestMethod.POST)
    public JsonResult uploadPicture(HttpServletRequest request) {
        return ResultUtils.success(toolService.uploadPicture(request));
    }


    @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    public JsonResult readTool(@RequestParam("key") String key,@RequestParam("value") String value){
        return ResultUtils.success(toolService.readTool(key,value));
    }

    @RequestMapping(value = "/findByProvider/{provider}", method = RequestMethod.GET)
    public JsonResult readAllProject(@PathVariable("provider") String provider) {
        return ResultUtils.success(toolService.findAllByProvider(provider));
    }

}
