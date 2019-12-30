package cn.edu.njnu.geoproblemsolving.Controller.Remote;

import cn.edu.njnu.geoproblemsolving.Entity.ModelTools.CModel.support.JsonResult;
import cn.edu.njnu.geoproblemsolving.Service.Remote.ModelItemService;
import cn.edu.njnu.geoproblemsolving.Utils.ResultUtils;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ModelContainerController
 * @Description
 * @Author Zhiyi
 * @Date 2019/12/16  21:58
 * @Version 1.0.0
 */
@RestController
@RequestMapping(value = "/modelItem")
public class ModelContainerController {
    @Autowired
    ModelItemService modelItemService;

    @RequestMapping(value = "/getModelItem/{id}",method = RequestMethod.GET)
    public JsonResult getModelItem(@PathVariable("id") String id) {
        return ResultUtils.success(modelItemService.getModelItem(id));
    }

    @RequestMapping(value = "/getModelItems",method = RequestMethod.GET)
    public JsonResult getModelItems() {
        return ResultUtils.success(modelItemService.getModelItems());
    }

    @RequestMapping(value = "/getModelInstance/{id}",method = RequestMethod.GET)
    public JsonResult getModelInstance(@PathVariable("id") String id) {
        return ResultUtils.success(modelItemService.getModelInstance(id));
    }

    @RequestMapping(value = "/addModelInstance",method = RequestMethod.POST)
    public JsonResult addModelInstance(@RequestBody JSONObject modelinstance) {
        return ResultUtils.success(modelItemService.addModelInstance(modelinstance));
    }

    @RequestMapping(value = "/modelInstance/{id}/invoke",method = RequestMethod.POST)
    public JsonResult invoke(@PathVariable("id") String id) throws InterruptedException {
        return ResultUtils.success(modelItemService.invokeModelInstance(id));
    }

    @RequestMapping(value = "/saveRecord",method = RequestMethod.POST)
    public JsonResult saveRecord(@RequestBody JSONObject toolRecord) {
        return ResultUtils.success(modelItemService.saveToolRecord(toolRecord));
    }

    @RequestMapping(value = "/getAllRecords/{stepId}",method = RequestMethod.GET)
    public JsonResult getAllRecords(@PathVariable("stepId") String stepId) {
        return ResultUtils.success(modelItemService.getAllRecords(stepId));
    }

    @RequestMapping(value = "/activity",method = RequestMethod.POST)
    public JsonResult activate(@RequestBody JSONObject obj) {
        return ResultUtils.success(modelItemService.activate(obj));
    }



}
