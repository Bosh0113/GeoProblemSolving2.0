package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.processDriven.service.impl.GeoAnalysisProcessImpl;
import cn.edu.njnu.geoproblemsolving.business.activity.service.Impl.ProtocolServiceImpl;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import cn.edu.njnu.geoproblemsolving.common.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    ProtocolServiceImpl protocolService;

    @Autowired
    GeoAnalysisProcessImpl analysisProcess;

    /**
     * inquiry link protocol
     */
    @RequestMapping(value = "/{pid}", method = RequestMethod.GET)
    public JsonResult inquiryActivityLinkProtocol(@PathVariable("pid") String pid){
        LinkProtocol protocol = this.protocolService.findProtocolService(pid);
        return ResultUtils.success(protocol);
    }

    /**
     * create link protocol
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult createActivityLinkProtocol(@RequestBody LinkProtocol linkProtocol){
        LinkProtocol protocol = protocolService.setProtocolService(linkProtocol);
        if (protocol != null){
            return ResultUtils.success(protocol);
        }
        return ResultUtils.error(-2, "Fail");
    }

    /**
     * delete link protocol
     */
    @RequestMapping(value = "/{pid}", method = RequestMethod.DELETE)
    public JsonResult removeActivityLinkProtocol(@PathVariable("pid") String pid){
        String delResult = protocolService.deleteProtocolService(pid);
        if (delResult.equals("suc")){
            return ResultUtils.success();
        }
        return ResultUtils.error(-2, "Fail, Link protocol id doesn't exist.");
    }

    /**
     * update link protocol
     */
    @RequestMapping(value = "/{pid}", method = RequestMethod.PUT)
    public JsonResult updateActivityLinkProtocol(@PathVariable("pid") String pid, @RequestBody HashMap<String,Object> putInfo)
    {
        LinkProtocol putResult = protocolService.updateProtocolService(pid, putInfo);
        if (putResult != null){
            return ResultUtils.success(putResult);
        }
        return ResultUtils.error(-2, "Fail, Protocol id doesn't exist.");
    }

}
