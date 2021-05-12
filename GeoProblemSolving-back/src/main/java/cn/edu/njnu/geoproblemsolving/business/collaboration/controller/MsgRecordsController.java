package cn.edu.njnu.geoproblemsolving.business.collaboration.controller;

import cn.edu.njnu.geoproblemsolving.business.collaboration.service.MsgRecordsService;
import cn.edu.njnu.geoproblemsolving.common.utils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*",allowCredentials = "true")
@RestController
@RequestMapping("/msgRecords")
public class MsgRecordsController {

    @Autowired
    MsgRecordsService msgRecordsService;

    @RequestMapping(value = "/{aid}", method = RequestMethod.GET)
    public JsonResult inquiryMsgRecords(@PathVariable("aid") String aid){
        return msgRecordsService.findMsgRecords(aid);
    }
}
