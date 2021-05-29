package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ActivityDocService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/activityDoc")
public class ActivityDocController {

    private final ActivityDocService activityDocService;

    public ActivityDocController(ActivityDocService activityDocService) {
        this.activityDocService = activityDocService;
    }

    /**
     * inquiry activity document
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult inquiryActivityDoc(@RequestParam("aid") String aid){
        return activityDocService.findDocument(aid);
    }

    /**
     * create activity document
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult createActivityDoc(@RequestBody ActivityDoc activityDoc){
        return activityDocService.saveDocument(activityDoc);
    }

    /**
     * delete activity document
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public JsonResult removeActivityDoc(@RequestParam("aid") String aid){
        return activityDocService.deleteDocument(aid);
    }

    /**
     * update activity document
     */
    @RequestMapping(method = RequestMethod.PUT)
    public JsonResult updateAcitivityDoc(@RequestBody  ActivityDoc activityDoc)
    {
        return activityDocService.updateDocument(activityDoc.getAid(), activityDoc.getDocument());
    }
}
