package cn.edu.njnu.geoproblemsolving.business.activity.controller;

import cn.edu.njnu.geoproblemsolving.business.activity.dto.UpdateProtocolDTO;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.ActivityDoc;
import cn.edu.njnu.geoproblemsolving.business.activity.entity.LinkProtocol;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ActivityDocService;
import cn.edu.njnu.geoproblemsolving.business.activity.service.ProtocolService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowCredentials = "true")
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    private final ProtocolService protocolService;

    public ProtocolController(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    /**
     * inquiry link protocol
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult inquiryActivityDoc(@RequestParam("pid") String pid){
        return protocolService.findProtocol(pid);
    }

    /**
     * create link protocol
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult createActivityDoc(@RequestBody LinkProtocol linkProtocol){
        return protocolService.setProtocol(linkProtocol);
    }

    /**
     * delete link protocol
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public JsonResult removeActivityDoc(@RequestParam("pid") String pid){
        return protocolService.deleteProtocol(pid);
    }

    /**
     * update link protocol
     */
    @RequestMapping(method = RequestMethod.PUT)
    public JsonResult updateAcitivityDoc(@RequestParam("pid") String pid, @RequestBody UpdateProtocolDTO updateProtocolDTO)
    {
        return protocolService.updateProtocol(pid, updateProtocolDTO);
    }
}
