package cn.edu.njnu.geoproblemsolving.business.tool.generalTool.controller;

import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.Tool;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.entity.ToolSetVo;
import cn.edu.njnu.geoproblemsolving.business.tool.generalTool.service.ToolService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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

    /**
     * 新建工具
     * 同新建工具集差别较大、分开为两个接口
     *
     * @param toolJson
     * @return
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public JsonResult createToolOrToolSet(@RequestBody JSONObject toolJson) throws UnsupportedEncodingException {
        Tool tool = toolService.createTool(toolJson);
        if (tool != null){
            return ResultUtils.success(tool);
        }
        return ResultUtils.error(-2, "Fail");
    }

    /**
     * 创建工具集
     * @param toolSet
     * @return
     */
    @RequestMapping(value = "/toolset", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    public JsonResult createToolSet(@RequestBody Tool toolSet){
        ToolSetVo toolSetVo = toolService.createToolSet(toolSet);
        if (toolSetVo == null) return ResultUtils.error(-2, "Fail");
        return ResultUtils.success(toolSetVo);
    }

    /**
     * 根据 id 查询
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public JsonResult getToolByTid(@PathVariable String id){
        Tool tool = toolService.getToolByTid(id);
        if (tool != null){
            return ResultUtils.success(tool);
        }
        return ResultUtils.error(-2, "Fail");
    }

    /**
     * 根据字段查询
     * 支持同时满足多字段
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/{key}/{value}", method = RequestMethod.GET)
    public JsonResult queryToolSet(@PathVariable ArrayList<String> key, @PathVariable ArrayList<String> value){
        List<Tool> toolSets = toolService.queryTool(key, value);
        if (toolSets != null) return ResultUtils.success(toolSets);
        return ResultUtils.error(-2, "Fail");
    }


    /**
     * 根据 id 删除
     * @param tid
     * @return
     */
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
    public JsonResult getToolListByToolIds(@PathVariable HashSet<String> ids){
        List<Tool> toolList = toolService.getToolByIds(ids);
        return ResultUtils.success(toolList);
    }


//================toolSet 操作==========================================================================================/



    /**
     * 删除数据集
     * @param tid
     * @return
     */
    @RequestMapping(value = "/toolSet/{tid}", method = RequestMethod.DELETE)
    public JsonResult deleteToolSet(@PathVariable String tid){
        toolService.delToolService(tid);
        return ResultUtils.success();
    }




    /**
     * 处理工具集的工具
     * 添加工具
     * @param toolSetId 工具集 id
     * @param tids 要添加的工具 id，若有多个则用 “,” 分开
     * @return 工具集
     */
    @RequestMapping(value = "/toolSet/tool/{toolSetId}/{tids}", method = RequestMethod.POST)
    public JsonResult addTool(@PathVariable String toolSetId, @PathVariable ArrayList<String> tids){
        ToolSetVo toolSetVo = toolService.addToolInToolSet(toolSetId, tids);
        if (toolSetVo == null) return ResultUtils.error(-2, "Toolset id is incorrect");
        return ResultUtils.success(toolSetVo);
    }

    /**
     *
     * 处理工具集的工具
     * 删除工具
     * 参数说明与上相同
     * @param toolSetId
     * @param tids
     * @return
     */
    @RequestMapping(value = "/toolSet/tool/{toolSetId}/{tids}", method = RequestMethod.DELETE)
    public JsonResult delTool(@PathVariable String toolSetId, @PathVariable ArrayList<String> tids){
        ToolSetVo toolSetVo = toolService.delToolInToolSet(toolSetId, tids);
        if (toolSetVo == null) return ResultUtils.error(-2, "Toolset id is incorrect");
        return ResultUtils.success(toolSetVo);
    }






}
