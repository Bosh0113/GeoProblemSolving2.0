package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.controller;

import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.service.ToolService;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.service.ToolServiceImpl;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ToolController
 * @Description tool 相关接口
 * 面向门户、模型容器、数据容器开发
 * 门户：模型与数据方法条目从门户读取
 * 管理服务容器与模型服务容器：模型运行
 * 数据容器：数据方法运行（后续数据容器中运算迁移到模型容器中的话，运行这部分需要修改）
 * @Author zhngzhng
 * @Date 2021/5/13
 **/
@RestController
@RequestMapping(value = "/tool")
public class ToolRestController {
    @Autowired
    ToolService toolService;

    /**
     * 从管理服务器获取公开的计算模型/数据方法用于创建工具
     * @param req
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/computableModel/public/{page}/{size}", method = RequestMethod.GET)
    public JsonResult getPublicModel(HttpServletRequest req, @PathVariable int page, @PathVariable int size){
        String email = (String)req.getSession().getAttribute("email");
        if (email == null){
            return ResultUtils.error(-2, "fail");
        }
        JSONObject computableModelList = toolService.getComputableModelList(email, page, size);
        return ResultUtils.success(computableModelList);
    }

    @RequestMapping(value = "/dataMethod/public/{page}/{size}", method = RequestMethod.GET)
    public JsonResult getDataMethodList(HttpServletRequest req, @PathVariable int page, @PathVariable int size){
        String email = (String)req.getSession().getAttribute("email");
        if (email == null){
            return ResultUtils.error(-2, "fail");
        }
        JSONObject dataMethodList = toolService.getDataMethodList(email, page, size);
        return ResultUtils.success(dataMethodList);
    }

    /**
     * 从管理服务器搜索计算模型/数据方法
     * @param searchText
     * @param page
     * @return
     */
    @RequestMapping(value = "/computableModel/public/{searchText}/{page}/{size}", method = RequestMethod.GET)
    public JsonResult queryPublicComputableModelByName(@PathVariable String searchText, @PathVariable int page){
        JSONObject modelData = toolService.queryPublicComputeModelByName(searchText, page);
        return ResultUtils.success(modelData);
    }


    @RequestMapping(value = "/dataMethod/public/{searchText}/{page}/{size}",method = RequestMethod.GET)
    public JsonResult queryPublicDataMethodByName(@PathVariable String searchText, @PathVariable int page){
        JSONObject dataMethodData = toolService.queryPublicDataMethodByName(searchText, page);
        return ResultUtils.success(dataMethodData);
    }





    /*
    工具创建成功后的一系列操作
    第一类 computableModel
    1. Invoke 页面内容
    2. Init
    3. Invoke
    4. task 监控

    第二类 DataMethod
    没有执行状态查询

    全部使用restful API 风格
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public JsonResult createToolOrToolSet(@RequestBody JSONObject toolJson) throws UnsupportedEncodingException {
        Tool tool = toolService.createTool(toolJson);
        if (tool != null){
            return ResultUtils.success(tool);
        }
        return ResultUtils.error(-2, "Fail");
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult getToolByTid(@PathVariable String id){
        Tool tool = toolService.getToolByTid(id);
        if (tool != null){
            return ResultUtils.success(tool);
        }
        return ResultUtils.error(-2, "Fail");
    }

    @RequestMapping(value = "/{tid}", method = RequestMethod.DELETE)
    public JsonResult delTool(@PathVariable String tid){
        toolService.delToolService(tid);
        return ResultUtils.success();
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json;charset=utf-8")
    public JsonResult updateToolInfo(@RequestBody Tool putTool){
        return ResultUtils.success(toolService.updateToolService(putTool));
    }


    /**
     * 返回该用户未删除的工具
     * @param providerId
     * @return
     */
    @RequestMapping(value = "/provider/{providerId}", method = RequestMethod.GET)
    public JsonResult queryToolByUserId(@PathVariable String providerId){
        return ResultUtils.success(toolService.getToolByProviderService(providerId));
    }

    /**
     * 返回工具列表
     * @param ids
     * @return
     */
    @RequestMapping(value = "/all/{ids}", method = RequestMethod.GET)
    public JsonResult getToolListByToolIds(@PathVariable ArrayList<String> ids){
        List<Tool> toolList = toolService.getToolByIds(ids);
        return ResultUtils.success(toolList);
    }
}
