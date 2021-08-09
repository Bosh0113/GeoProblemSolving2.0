package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityGraph;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.ActivityRelation;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.entity.LinkRestriction;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.setvice.impl.GeoAnalysisProcessImpl;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @ClassName GeoActivityDriveController
 * @Description 地理活动驱动接口
 * @Author zhngzhng
 * @Date 2021/8/3
 **/
@RequestMapping(value = "/ActivityDriven")
@RestController
public class GeoActivityDriveController {
    private final GeoAnalysisProcessImpl geoAnalysisProcess;

    public GeoActivityDriveController(GeoAnalysisProcessImpl geoAnalysisProcess) {
        this.geoAnalysisProcess = geoAnalysisProcess;
    }

    @RequestMapping(value = "/graph/{rootId}", method = RequestMethod.GET)
    public JsonResult initGraph(@PathVariable String rootId){
        ActivityGraph activityGraph = geoAnalysisProcess.initActivityGraph(rootId);
        return ResultUtils.success(activityGraph);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    public JsonResult setActivityRelation(@RequestBody ActivityRelation relation, @RequestBody LinkRestriction restriction){
        String id = relation.getGraphId();
        ArrayList<String> nodeList = relation.getNodes();
        String type = relation.getType();
        geoAnalysisProcess.setLinkProtocol(id, type, nodeList, restriction);
        return null;
    }
}
