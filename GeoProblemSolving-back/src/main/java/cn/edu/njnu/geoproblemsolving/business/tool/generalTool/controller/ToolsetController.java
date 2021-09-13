package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.controller;

import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.dao.impl.ToolsetDaoImpl;

import cn.edu.njnu.geoproblemsolving.Entity.ToolReq.AddToolReq;
import cn.edu.njnu.geoproblemsolving.Entity.ToolReq.UpdateToolListReq;
import cn.edu.njnu.geoproblemsolving.Entity.ToolsetEntity;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.ToolSetVo;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.service.ToolService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
* @Author mzy
* @Description(zhngzhng)  工具集
* @Date 2021/7/1
*/
@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/toolset")
public class ToolsetController {

    @Resource
    private MongoTemplate mongoTemplate;

    @Autowired
    ToolService toolService;

    // @RequestMapping(value = "/create", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // public Object createToolset(@RequestBody ToolsetEntity toolset){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     try {
    //         return toolsetDao.createToolset(toolset);
    //     }catch (Exception e){
    //         return "Fail";
    //     }
    // }

    @RequestMapping(method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult createToolSet(@RequestBody Tool toolSet){
        ToolSetVo toolSetVo = toolService.createToolSet(toolSet);
        if (toolSetVo == null) return ResultUtils.error(-2, "Fail");
        return ResultUtils.success(toolSetVo);
    }

    // @RequestMapping(value = "/inquiry", method = RequestMethod.GET)
    // public Object readToolset(@RequestParam("key") String key, @RequestParam("value") String value){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     try {
    //         return toolsetDao.readToolset(key,value);
    //     }catch (Exception e){
    //         return "Fail";
    //     }
    // }

    @RequestMapping(value = "/{key}/{value}", method = RequestMethod.GET)
    public JsonResult queryToolSet(@PathVariable ArrayList<String> key, @PathVariable ArrayList<String> value){
        List<Tool> toolSets = toolService.queryTool(key, value);
        if (toolSets != null) return ResultUtils.success(toolSets);
        return ResultUtils.error(-2, "Fail");
    }


    // @RequestMapping(value = "/inquiryAll", method = RequestMethod.GET)
    // public Object readAllProject(@RequestParam("provider") String provider) {
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     return toolsetDao.readAccessibleToolsets(provider);
    // }

    @RequestMapping(value = "/{provider}", method = RequestMethod.GET)
    public JsonResult readUserToolsets(@PathVariable String provider){
        List<Tool> toolByProviderService = toolService.getToolByProviderService(provider);
        if (toolByProviderService != null) return ResultUtils.success(toolByProviderService);
        return ResultUtils.error(-2, "Fail");
    }

    // @RequestMapping(value = "/delete", method = RequestMethod.GET)
    // public String deleteToolset(@RequestParam("tsId") String tsId){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     try {
    //         toolsetDao.deleteToolset("tsid",tsId);
    //         return "Success";
    //     }catch (Exception e){
    //         return "Fail";
    //     }
    // }

    @RequestMapping(value = "/{tid}", method = RequestMethod.DELETE)
    public JsonResult deleteToolSet(@PathVariable String tid){
        toolService.delToolService(tid);
        return ResultUtils.success();
    }


    /**
     * 上传工具集图片
     * @param request
     * @return 图片地址
     * @throws IOException
     * @throws ServletException
     */
    @RequestMapping(value = "/img", method = RequestMethod.POST)
    public JsonResult uploadPicture(HttpServletRequest request) throws IOException, ServletException {
        return toolService.uploadToolImg(request);
    }


    // @RequestMapping(value = "/update", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // public String updateToolset(HttpServletRequest request){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     return toolsetDao.updateToolset(request);
    // }

    /**
     * 更新工具集
     * 必须携带 toolSetId
     * 将需要修改的字段携带过来即可
     * @param putToolSet
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, produces = {"application/json;charset=UTF-8"})
    public JsonResult updateToolSet(@RequestBody Tool putToolSet){
        Tool tool = toolService.updateToolService(putToolSet);
        if (tool == null) return ResultUtils.error(-2, "Fail");
        ToolSetVo toolSetVo = new ToolSetVo();
        BeanUtils.copyProperties(tool, toolSetVo);
        return ResultUtils.success(toolSetVo);
    }


    // @RequestMapping(value = "/updateTools", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // public String updateTools(@RequestBody UpdateToolListReq updateToolListReq){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     return toolsetDao.updateToolList(updateToolListReq);
    // }


    // @RequestMapping(value = "/addTool", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // public String addTool(@RequestBody AddToolReq addToolReq){
    //     ToolsetDaoImpl toolsetDao = new ToolsetDaoImpl(mongoTemplate);
    //     return toolsetDao.addToolToToolset(addToolReq.getNewTool(),addToolReq.getTsIds());
    // }

    /**
     * 处理工具集的工具
     * 添加工具
     * @param toolSetId
     * @param tids
     * @return
     */
    @RequestMapping(value = "/tool/{toolSetId}/{tids}", method = RequestMethod.POST)
    public JsonResult addTool(@PathVariable String toolSetId, @PathVariable ArrayList<String> tids){
        ToolSetVo toolSetVo = toolService.addToolInToolSet(toolSetId, tids);
        if (toolSetVo == null) return ResultUtils.error(-2, "Toolset id is incorrect");
        return ResultUtils.success(toolSetVo);
    }

    /**
     * 处理工具集的工具
     * 删除工具
     * @param toolSetId
     * @param tids
     * @return
     */
    @RequestMapping(value = "/tool/{toolSetId}/{tids}", method = RequestMethod.DELETE)
    public JsonResult delTool(@PathVariable String toolSetId, @PathVariable ArrayList<String> tids){
        ToolSetVo toolSetVo = toolService.delToolInToolSet(toolSetId, tids);
        if (toolSetVo == null) return ResultUtils.error(-2, "Toolset id is incorrect");
        return ResultUtils.success(toolSetVo);
    }
}
